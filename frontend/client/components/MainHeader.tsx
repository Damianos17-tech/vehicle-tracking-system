import { Truck, X, Menu } from "lucide-react";

type TruckType = {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: "active" | "idle" | "inactive";
  name: string;
};

interface Props {
  trucks: TruckType[];
  sidebarOpen: boolean;
  toggleSidebar: () => void;
  onHomeClick: () => void;
  messagesPerSecond: number;
}

export default function MainHeader({
  trucks,
  sidebarOpen,
  toggleSidebar,
  onHomeClick,
  messagesPerSecond
}: Props) {
	//
	

	//const navigate = useNavigate();
  return (
    <div className="px-6 py-2">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        
        {/* LEFT SIDE */}
        <div className="flex items-center gap-3 cursor-pointer" onClick={onHomeClick}>
          <div className="bg-white bg-opacity-100 backdrop-blur-sm p-2 rounded-lg text-black">
            <Truck className="w-6 h-6" />
          </div>

          <div>
            <h1 className="text-2xl font-bold text-white">FleetOps Tracker</h1>
            <p className="text-blue-100 text-sm">
              Real-time vehicle monitoring
            </p>
          </div>
        </div>

        {/* RIGHT SIDE */}
        <div className="flex items-center gap-4 pr-0">
          <div className="text-right">
            <p className="text-md text-blue-100">
			{/*Active Vehicles: {trucks.length} Build 2026.07.08 */}
			  
			  Version 1.1.5 
            </p>
			
		  <div className="flex items-center gap-4 pr-0 text-white">
			WebSocket: 📡 {messagesPerSecond} msg/s
		  </div>
          </div>

			<button
			  onClick={toggleSidebar}
			  className="sidebar-toggle md:hidden bg-white bg-opacity-10 backdrop-blur-sm p-2 rounded-lg hover:bg-opacity-20 transition-all shadow-lg"
			>
            {sidebarOpen ? (
              <X className="w-6 h-6" />
            ) : (
              <Menu className="w-6 h-6" />
            )}
          </button>
        </div>

      </div>
    </div>
  );
}