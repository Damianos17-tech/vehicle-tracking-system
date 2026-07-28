import L from "leaflet";

export const getTruckIcon = () => {
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
        <svg xmlns="http://www.w3.org/2000/svg"
             width="22"
             height="22"
             viewBox="0 0 64 64"
             transform="scale(1.2)">
          <rect x="8" y="26" width="30" height="16" rx="3" fill="#dc2626"/>
          <rect x="38" y="28" width="14" height="14" rx="2" fill="#fbbf24"/>
          <rect x="41" y="31" width="7" height="5" rx="1" fill="#60a5fa"/>
          <rect x="8" y="38" width="44" height="2" fill="#991b1b" opacity="0.6"/>
          <circle cx="18" cy="44" r="4" fill="#111827"/>
          <circle cx="44" cy="44" r="4" fill="#111827"/>
          <rect x="52" y="34" width="6" height="3" fill="#9ca3af"/>
        </svg>
      </div>
    `,
    className: "truck-icon",
    iconSize: [40, 40],
    iconAnchor: [20, 40],
    popupAnchor: [0, -40],
  });
};




export const getStatusColor = (status: string) => {
  switch (status) {
    case "ACTIVE":
      return "bg-emerald-500";

    case "WARNING":
      return "bg-yellow-500";

    case "FAILURE":
      return "bg-red-600";

    case "INACTIVE":
      return "bg-slate-400";

    default:
      return "bg-slate-400";
  }
};

export const getStatusLabel = (status: string) => {
  switch (status) {
    case "ACTIVE":
      return "Active";

    case "WARNING":
      return "Warning";

    case "FAILURE":
      return "Failure";

    case "INACTIVE":
      return "Inactive";

    default:
      return status;
  }
};

interface TruckData {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: "ACTIVE" | "WARNING" | "FAILURE" | "INACTIVE";
  name: string;
};


export type Truck = {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: "ACTIVE" | "WARNING" | "FAILURE" | "INACTIVE";
  name: string;
};


export interface SidebarProps {
  sidebarOpen: boolean;
  activeSection: SectionType;
  setActiveSection: (v: SectionType) => void;

  mapSubView: MapSubView;
  setMapSubView: (v: MapSubView) => void;

  trucks: Truck[];

  selectedTruck: string | null;
  setSelectedTruck: (v: string | null) => void;

  map: React.RefObject<L.Map | null>;
  markersRef: React.RefObject<Record<string, L.Marker>>;

  getStatusColor: (s: string) => string;
  getStatusLabel: (s: string) => string;

  handleSidebarInteraction: () => void;
}

































