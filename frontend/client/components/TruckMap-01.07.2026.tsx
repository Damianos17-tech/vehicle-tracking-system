import { useEffect, useRef, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Truck, MapPin, Zap, Map as MapIcon, Activity, Settings, Shield, ChevronRight, Menu, X, Search } from 'lucide-react';
import Sidebar from "./Sidebar";
import MainHeader from "./MainHeader";
import TopStatusBar from "./TopStatusBar";

import { useTruckStream } from "./hooks/useTruckStream";

interface TruckData {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: 'active' | 'idle' | 'inactive';
  name: string;
}

const truckPositionsRef = useRef<{ [key: string]: { lat: number; lng: number } }>({});

// Aleje Jerozolimskie route points (West to East)
const ALEJE_JEROZOLIMSKIE_ROUTE = [
  { lat: 52.2330, lng: 20.9800 },
  { lat: 52.2325, lng: 20.9900 },
  { lat: 52.2320, lng: 21.0000 },
  { lat: 52.2315, lng: 21.0100 },
  { lat: 52.2310, lng: 21.0200 },
  { lat: 52.2305, lng: 21.0300 },
  { lat: 52.2300, lng: 21.0400 },
  { lat: 52.2295, lng: 21.0500 },
];

// Marszałkowska route points (North to South)
const MARSZALKOWSKA_ROUTE = [
  { lat: 52.2450, lng: 21.0122 },
  { lat: 52.2420, lng: 21.0125 },
  { lat: 52.2390, lng: 21.0128 },
  { lat: 52.2360, lng: 21.0130 },
  { lat: 52.2330, lng: 21.0133 },
  { lat: 52.2300, lng: 21.0135 },
  { lat: 52.2270, lng: 21.0137 },
  { lat: 52.2240, lng: 21.0140 },
];

const trucks: TruckData[] = [
  {
    id: 'TRUCK-001',
    lat: 52.2297,
    lng: 21.0122,
    speed: 65,
    status: 'active',
    name: 'Truck Alpha',
  },
  {
    id: 'TRUCK-002',
    lat: 52.2350,
    lng: 21.0200,
    speed: 45,
    status: 'active',
    name: 'Truck Beta',
  },
  {
    id: 'TRUCK-003',
    lat: 52.2200,
    lng: 21.0100,
    speed: 0,
    status: 'idle',
    name: 'Truck Gamma',
  },
];

const getTruckIcon = () => {
  return L.divIcon({
    html: `
      <div style="
        background: linear-gradient(135deg, #5865f2 0%, #1a2136 100%);
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 0 20px rgba(88, 101, 242, 0.6), 0 4px 12px rgba(26, 33, 54, 0.4);
        border: 3px solid white;
      ">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="white" xmlns="http://www.w3.org/2000/svg">
          <path d="M18 10H6V8H18M6 14H18V12H6M6 18H18V16H6Z"/>
        </svg>
      </div>
    `,
    className: 'truck-icon',
    iconSize: [40, 40],
    iconAnchor: [20, 40],
    popupAnchor: [0, -40],
  });
};

const getStatusColor = (status: string) => {
  switch (status) {
    case 'active':
      return 'bg-emerald-500';
    case 'idle':
      return 'bg-amber-500';
    default:
      return 'bg-slate-400';
  }
};

const getStatusLabel = (status: string) => {
  switch (status) {
    case 'active':
      return 'Active';
    case 'idle':
      return 'Idle';
    default:
      return 'Inactive';
  }
};



export default function TruckMap() {
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<L.Map | null>(null);
  const [selectedTruck, setSelectedTruck] = useState<string | null>(null);
  const [activeSection, setActiveSection] = useState<SectionType>('mapa');
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [mapSubView, setMapSubView] = useState<'mapa' | 'pojazdy'>('mapa');
  const [truck001Position, setTruck001Position] = useState({ lat: 52.2297, lng: 21.0122 });
  const [truck002Position, setTruck002Position] = useState({ lat: 52.2350, lng: 21.0200 });
  const [searchQuery, setSearchQuery] = useState('');
  const markersRef = useRef<{ [key: string]: L.Marker }>({});
  const autoHideTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  const route001IndexRef = useRef(0);
  const route002IndexRef = useRef(0);

  const handleSidebarInteraction = () => {
    // Reset auto-hide timeout on interaction
    if (autoHideTimeoutRef.current) {
      clearTimeout(autoHideTimeoutRef.current);
    }

    // Set new timeout to hide after 10 seconds
    autoHideTimeoutRef.current = setTimeout(() => {
      setSidebarOpen(false);
    }, 10000);
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
    // Reset timeout when manually toggling
    if (autoHideTimeoutRef.current) {
      clearTimeout(autoHideTimeoutRef.current);
    }
    if (!sidebarOpen) {
      // If opening, set timeout to auto-hide
      autoHideTimeoutRef.current = setTimeout(() => {
        setSidebarOpen(false);
      }, 10000);
    }
  };

  // Tracking animation for TRUCK-001 and TRUCK-002
  useEffect(() => {
    const movementInterval = setInterval(() => {
      // TRUCK-001 movement
      setTruck001Position((prev) => {
        const nextIndex = (route001IndexRef.current + 1) % ALEJE_JEROZOLIMSKIE_ROUTE.length;
        route001IndexRef.current = nextIndex;
        return ALEJE_JEROZOLIMSKIE_ROUTE[nextIndex];
      });

      // TRUCK-002 movement
      setTruck002Position((prev) => {
        const nextIndex = (route002IndexRef.current + 1) % MARSZALKOWSKA_ROUTE.length;
        route002IndexRef.current = nextIndex;
        return MARSZALKOWSKA_ROUTE[nextIndex];
      });
    }, 2000); // Move every 2 seconds

    return () => clearInterval(movementInterval);
  }, []);

  // Update TRUCK-001 and TRUCK-002 marker positions
  useEffect(() => {
    const marker001 = markersRef.current['TRUCK-001'];
    if (marker001) {
      marker001.setLatLng([truck001Position.lat, truck001Position.lng]);

      // Auto-follow selected truck
      if (selectedTruck === 'TRUCK-001' && map.current) {
        map.current.setView([truck001Position.lat, truck001Position.lng], 13);
      }
    }

    const marker002 = markersRef.current['TRUCK-002'];
    if (marker002) {
      marker002.setLatLng([truck002Position.lat, truck002Position.lng]);

      // Auto-follow selected truck
      if (selectedTruck === 'TRUCK-002' && map.current) {
        map.current.setView([truck002Position.lat, truck002Position.lng], 13);
      }
    }
  }, [truck001Position, truck002Position, selectedTruck]);

  useEffect(() => {
    if (!mapContainer.current) return;

    // Initialize map
    map.current = L.map(mapContainer.current).setView([52.2297, 21.0122], 13);

    // Add OpenStreetMap tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19,
    }).addTo(map.current);

    // Add truck markers
    trucks.forEach((truck) => {
      const marker = L.marker([truck.lat, truck.lng], {
        icon: getTruckIcon(),
      }).addTo(map.current!);

      const popupContent = document.createElement('div');
      popupContent.className = 'w-64 p-4';
      popupContent.innerHTML = `
        <div>
          <h3 style="font-weight: bold; font-size: 18px; color: #1f2937; margin-bottom: 12px;">
            ${truck.name}
          </h3>
          <div style="display: flex; flex-direction: column; gap: 12px;">
            <div style="display: flex; gap: 12px; padding: 12px; background: #f8fafc; border-radius: 8px;">
              <div style="flex-shrink: 0;">📍</div>
              <div>
                <p style="font-size: 11px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 4px;">
                  Vehicle ID
                </p>
                <p style="font-family: monospace; font-size: 14px; font-weight: bold; color: #1f2937;">
                  ${truck.id}
                </p>
              </div>
            </div>
            <div style="display: flex; gap: 12px; padding: 12px; background: #f8fafc; border-radius: 8px;">
              <div style="flex-shrink: 0;">⚡</div>
              <div>
                <p style="font-size: 11px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 4px;">
                  Speed
                </p>
                <p style="font-family: monospace; font-size: 14px; font-weight: bold; color: #1f2937;">
                  ${truck.speed} km/h
                </p>
              </div>
            </div>
            <div style="padding: 12px; background: #f8fafc; border-radius: 8px;">
              <p style="font-size: 11px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 8px;">
                Status
              </p>
              <div style="display: flex; gap: 8px; align-items: center;">
                <div style="width: 12px; height: 12px; border-radius: 50%; ${truck.status === 'active' ? 'background: #10b981;' : truck.status === 'idle' ? 'background: #f59e0b;' : 'background: #94a3b8;'} animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;"></div>
                <span style="font-weight: bold; font-size: 14px; color: #1f2937;">
                  ${getStatusLabel(truck.status)}
                </span>
              </div>
            </div>
          </div>
        </div>
      `;

      marker.bindPopup(popupContent).on('click', () => {
        setSelectedTruck(truck.id);
      });

      markersRef.current[truck.id] = marker;
    });

    // Deselect truck when clicking on map (not on marker)
    map.current.on('click', (e: L.LeafletMouseEvent) => {
      // Check if click was on a marker
      const clickedOnMarker = (e.originalEvent.target as HTMLElement).closest('.leaflet-marker-icon');
      if (!clickedOnMarker) {
        setSelectedTruck(null);
      }
    });

    // Set initial auto-hide timeout
    handleSidebarInteraction();

    return () => {
      if (map.current) {
        map.current.remove();
        map.current = null;
      }
      if (autoHideTimeoutRef.current) {
        clearTimeout(autoHideTimeoutRef.current);
      }
    };
  }, []);

  return (
    <div className="h-screen w-full flex flex-col bg-gradient-to-br from-slate-950 to-slate-900">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-900 via-slate-900 to-slate-950 text-white shadow-2xl">
	  
	  
	  
		<TopStatusBar
		  trucks={trucks}
		  searchQuery={searchQuery}
		  setSearchQuery={setSearchQuery}
		/>
	

        {/* Main Header */}
		
		{/*
		<MainHeader
		  trucks={trucks}
		  sidebarOpen={sidebarOpen}
		  toggleSidebar={toggleSidebar}
		/>
		{/**/}
		
		

      </div>

      {/* Main Content */}
      <div className="flex-1 flex overflow-hidden">
        {/* Map Container */}
        <div className="flex-1 relative">
          <div ref={mapContainer} className="w-full h-full" />
        </div>


	<Sidebar
	  sidebarOpen={sidebarOpen}
	  activeSection={activeSection}
	  setActiveSection={setActiveSection}
	  mapSubView={mapSubView}
	  setMapSubView={setMapSubView}
	  trucks={trucks}
	  selectedTruck={selectedTruck}
	  setSelectedTruck={setSelectedTruck}
	  map={map}
	  markersRef={markersRef}
	  getStatusColor={getStatusColor}
	  getStatusLabel={getStatusLabel}
	  handleSidebarInteraction={handleSidebarInteraction}
	/>
	
		
		
		
      </div>
    </div>
  );
}
