import { useEffect, useRef, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

import Sidebar from "./Sidebar";
import VehicleDetailsPanel from "./VehicleDetailsPanel";
import MainHeader from "./MainHeader";
import TopStatusBar from "./TopStatusBar";

import { animateMarker } from "../hooks/animateMarker";
//import { useTruckStream } from "../hooks/useTruckStream";
import { createTruckPopup } from "../hooks/truckPopup";
import { getTruckIcon } from "../hooks/truckIcon";
import { getStatusColor, getStatusLabel } from "../hooks/truckModel";

interface TruckMapProps {
  onBack: () => void;
  trucks: TruckData[];
  messagesPerSecond: number;
}


export default function TruckMap({
  onBack,
  trucks,
  messagesPerSecond,
}: TruckMapProps) {
	
	console.log("TRUCK MAP RENDER");
	
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<L.Map | null>(null);

  const markersRef = useRef<{ [key: string]: L.Marker }>({});
  //const autoHideTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  //const { trucks: trucksFromStream, messagesPerSecond } = useTruckStream();
  
const trucksSorted = [...trucks].sort((a, b) => {
  const getNum = (id: string) => {
    const match = id.match(/(\d+)$/);
    return match ? Number(match[1]) : 0;
  };

  return getNum(a.id) - getNum(b.id);
});
  
  
  {/*
useEffect(() => {
  console.log("TRUCKS UPDATE:", {
    count: trucks.length,
    first: trucks?.[0],
  });
}, [trucks.length]);

  */}
  

  const [selectedTruck, setSelectedTruck] = useState<string | null>(null);
  const [activeSection, setActiveSection] = useState<SectionType>("mapa");
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [mapSubView, setMapSubView] = useState<"mapa" | "pojazdy">("mapa");
  const [searchQuery, setSearchQuery] = useState("");
  const sidebarRef = useRef<HTMLDivElement>(null);
  
  const [detailsOpen, setDetailsOpen] = useState(false);
  
  const lastTargets = useRef<Map<string, L.LatLng>>(new Map());



		const toggleSidebar = () => {
		  setSidebarOpen((v) => !v);
		};
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  useEffect(() => {
  const handleClick = (e: MouseEvent | TouchEvent) => {
    if (!sidebarOpen) return;

			const target = e.target as HTMLElement;

			// kliknięcie hamburgera ignorujemy
			if (target.closest(".sidebar-toggle")) {
			  return;
			}

			// kliknięcie wewnątrz sidebaru ignorujemy
			if (sidebarRef.current?.contains(target)) {
			  return;
			}

			setSidebarOpen(false);
  };

  document.addEventListener("mousedown", handleClick);
  document.addEventListener("touchstart", handleClick);

  return () => {
    document.removeEventListener("mousedown", handleClick);
    document.removeEventListener("touchstart", handleClick);
  };
}, [sidebarOpen]);
  
  
  
  
  
  
  
  

  // =========================
  // INIT MAP
  // =========================
  useEffect(() => {
    if (!mapContainer.current || map.current) return;

    map.current = L.map(mapContainer.current).setView(
      [52.2297, 21.0122],
      13
    );

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "&copy; OpenStreetMap contributors",
      maxZoom: 19,
    }).addTo(map.current);

    map.current.on("click", (e) => {
      const clickedOnMarker = (e.originalEvent.target as HTMLElement)
        .closest(".leaflet-marker-icon");

      if (!clickedOnMarker) setSelectedTruck(null);
    });

    //handleSidebarInteraction();

    setTimeout(() => {
      map.current?.invalidateSize();
    }, 500);

    return () => {
      map.current?.remove();
      map.current = null;
    };
  }, []);

  // =========================
  // FIX MOBILE RESIZE
  // =========================
  useEffect(() => {
    if (!map.current) return;

    const t = setTimeout(() => {
      map.current?.invalidateSize();
    }, 300);

    return () => clearTimeout(t);
  }, []);
  
  
  useEffect(() => {

  if (!map.current) return;


  const activeTruckIds = new Set(
    trucks.map(t => t.id)
  );


  Object.keys(markersRef.current).forEach(id => {


    if (!activeTruckIds.has(id)) {


      console.log(
        "REMOVING MARKER:",
        id
      );


      const marker = markersRef.current[id];


      marker.remove();


      delete markersRef.current[id];


      lastTargets.current.delete(id);


      if (selectedTruck === id) {
        setSelectedTruck(null);
        setDetailsOpen(false);
      }

    }


  });


}, [trucks]);

  // =========================
  // CREATE + UPDATE MARKERS
  // =========================
  useEffect(() => {
    if (!map.current) return;
	
	for (const truck of trucks) {
  let marker = markersRef.current[truck.id];

  // CREATE MARKER
  if (!marker) {
    marker = L.marker([truck.lat, truck.lng], {
      icon: getTruckIcon(),
    }).addTo(map.current!);

    marker.bindPopup(createTruckPopup(truck));
	
	
	marker.on("click", () => {
	  setSelectedTruck(truck.id);
	  setDetailsOpen(true);
	});
		

    markersRef.current[truck.id] = marker;

    // zapisz pierwszą pozycję
    lastTargets.current.set(
      truck.id,
      L.latLng(truck.lat, truck.lng)
    );

    continue;
  }

  const target = L.latLng(truck.lat, truck.lng);

  // CACHE CHECK (czy coś się zmieniło)
  const prev = lastTargets.current.get(truck.id);

  if (prev && prev.lat === target.lat && prev.lng === target.lng) {
    // tylko popup update jeśli otwarty
    if (marker.isPopupOpen()) {
      marker.setPopupContent(createTruckPopup(truck));
    }
    continue;
  }

  // zapisz nową pozycję
  lastTargets.current.set(truck.id, target);

  const current = marker.getLatLng();

  // animacja ruchu
  animateMarker(marker, current, target, 600);

  // update popup (zawsze świeży content)
  marker.setPopupContent(createTruckPopup(truck));
}
	
	

	
	
	
	


  }, [trucksSorted]);
  
  
  
  // =========================
  // CLOSE RIGHT SIDEBAR AFTER 3 SECONDS
  // =========================
  
  
  
  useEffect(() => {
	  
	    const timer = setTimeout(() => {
    //setDetailsOpen(false);
	map.current.closePopup();
  }, 3000);


  return () => clearTimeout(timer);

}, [selectedTruck, detailsOpen]);


  // =========================
  // FOCUS SELECTED TRUCK
  // =========================
  useEffect(() => {
    if (!selectedTruck || !map.current) return;

    const marker = markersRef.current[selectedTruck];
    if (!marker) return;

    map.current.closePopup();

    map.current.flyTo(marker.getLatLng(), 13, {duration: 1.0,});
	

    const timer = setTimeout(() => {
      marker.openPopup();
    }, 1200);

    return () => clearTimeout(timer);
  }, [selectedTruck]);

  return (
    <div className="relative isolate h-[100dvh] flex flex-col overflow-hidden bg-gradient-to-br from-slate-950 to-slate-900">
      <div className="relative z-[50] bg-gradient-to-r from-indigo-900 via-slate-900 to-slate-950 text-white shadow-2xl">
        <TopStatusBar
          trucks={trucksSorted}
          searchQuery={searchQuery}
          setSearchQuery={setSearchQuery}
        />

        <MainHeader
          trucks={trucksSorted}
          sidebarOpen={sidebarOpen}
          toggleSidebar={toggleSidebar}
		  onHomeClick={onBack}
		  messagesPerSecond={messagesPerSecond}
        />
      </div>

      <div className="flex-1 flex overflow-hidden relative z-[60]">
	  
	  
	          <Sidebar
          sidebarOpen={sidebarOpen}
          activeSection={activeSection}
          setActiveSection={setActiveSection}
          mapSubView={mapSubView}
          setMapSubView={setMapSubView}
          trucks={trucksSorted}
          selectedTruck={selectedTruck}
          setSelectedTruck={setSelectedTruck}
          map={map}
          markersRef={markersRef}
          getStatusColor={getStatusColor}
          getStatusLabel={getStatusLabel}
          
		  sidebarRef={sidebarRef}
		  setSidebarOpen={setSidebarOpen}
	      setDetailsOpen={setDetailsOpen}
        />
	  
	  
        <div className="flex-1 relative z-0">
          <div ref={mapContainer} className="w-full h-full" />
        </div>


      </div>
	  
	  		
		  {/* PRAWY PANEL */}
<VehicleDetailsPanel
  selectedTruck={selectedTruck}
  trucks={trucksSorted}
  onSelectTruck={setSelectedTruck}
  detailsOpen={detailsOpen}
  setDetailsOpen={setDetailsOpen}
/>


	  
	  
	  
    </div>
  );
}