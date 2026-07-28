import { X, Zap, Droplet, Thermometer, MapPin } from 'lucide-react';
import TruckDetailsPanel from "./TruckDetailsPanel";
import { TruckCard, TruckCard2 } from "./TruckCard";

interface TruckData {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: 'active' | 'idle' | 'inactive';
  name: string;
}

interface VehicleDetailsPanelProps {
  selectedTruck: string | null;
  trucks: TruckData[];
  onSelectTruck: (truckId: string | null) => void;
}

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

export default function VehicleDetailsPanel({
  selectedTruck,
  trucks,
  onSelectTruck,
}: VehicleDetailsPanelProps) {
  const selectedTruckData = trucks.find((t) => t.id === selectedTruck);

  return (
    <div className="w-80 bg-gradient-to-b from-slate-900 via-indigo-950 to-slate-900 border-l border-indigo-600 border-opacity-50 flex flex-col shadow-2xl shadow-indigo-900/50 backdrop-blur-sm overflow-hidden">
      {/* Selected Truck Details */}
      {selectedTruckData ? (
        <div className="flex-1 overflow-y-auto p-4">
          <div className="mb-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-white">{selectedTruckData.name}</h2>
              <button
                onClick={() => onSelectTruck(null)}
                className="text-slate-400 hover:text-white transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
			
			
			  {/* ID */}
              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">
                <p className="text-xs text-slate-400 uppercase font-semibold mb-2">Vehicle ID</p>
                <p className="font-mono text-white text-sm">{selectedTruckData.id}</p>
              </div>

            <div className="space-y-3">
              {/* Speed */}
              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <Zap className="w-5 h-5 text-orange-400" />
                  <div className="flex-1">
                    <p className="text-xs text-slate-400 uppercase font-semibold">Speed</p>
                    <p className="text-lg font-bold text-white">{selectedTruckData.speed} km/h</p>
                  </div>
                </div>
              </div>

              {/* Fuel */}
              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <Droplet className="w-5 h-5 text-blue-400" />
                  <div className="flex-1">
                    <p className="text-xs text-slate-400 uppercase font-semibold">Fuel</p>
                    <p className="text-lg font-bold text-white">{selectedTruckData.fuelLevel}%</p>
                  </div>
                </div>
              </div>

              {/* Temperature */}
              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <Thermometer className="w-5 h-5 text-red-400" />
                  <div className="flex-1">
                    <p className="text-xs text-slate-400 uppercase font-semibold">Temp</p>
                    <p className="text-lg font-bold text-white">87°C</p>
                  </div>
                </div>
              </div>

              {/* Status */} {/*
              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <div className="flex-1">
                    <p className="text-xs text-slate-400 uppercase font-semibold">Status</p>
                    <div className="flex items-center gap-2 mt-1">
                      <div className={`w-2 h-2 rounded-full animate-pulse ${getStatusColor(selectedTruckData.status)}`}></div>
                      <span className="font-bold text-white">{getStatusLabel(selectedTruckData.status)}</span>
                    </div>
                  </div>
                </div>
              </div>  
			  */}


            </div>
          </div>
        </div>
      ) : (
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center p-6">
            <MapPin className="w-12 h-12 text-slate-500 mx-auto mb-3" />
            <p className="text-slate-400 text-sm">Kliknij na pojazd aby zobaczyć detale</p>
          </div>
        </div>
      )}

      {/* Vehicle List */}
	  
	  
	  
	                    <TruckDetailsPanel
                    truck={trucks.find((t: any) => t.id === selectedTruck)}
                    onBack={() => setSelectedTruck(null)}
                  />
	  
	  

	  {/* Workshop */}

<div className="mt-4 p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">

  <div className="flex items-center gap-2 mb-4">

    <span className="text-lg">🔧</span>

    <h3 className="text-sm font-bold text-white uppercase">
      Workshop
    </h3>

  </div>


  <div className="space-y-3">

  
    <div className="flex justify-between">
	
	{/*
      <span className="text-xs text-slate-400">
        Engine
      </span>

      <span className="text-sm text-emerald-400 font-semibold">
        OK
      </span>
    </div>


    <div className="flex justify-between">
      <span className="text-xs text-slate-400">
        Brakes
      </span>

      <span className="text-sm text-emerald-400 font-semibold">
        OK
      </span>
    </div>


    <div className="flex justify-between">
      <span className="text-xs text-slate-400">
        Oil level
      </span>
	  
  

      <span className="text-sm text-amber-400 font-semibold">
        42%
      </span>
	  
	  */}
    </div>


    <div className="flex justify-between">
      <span className="text-xs text-slate-400">
        Next service
      </span>

      <span className="text-sm text-white font-semibold">
        12 500 km
      </span>
    </div>


  </div>

</div>
	  
	  
	  
	  
    </div>
  );
}
