import { useEffect, useRef, useState } from "react";
import { X, Zap, Droplet, Thermometer, MapPin } from "lucide-react";
import TruckDetailsPanel from "./TruckDetailsPanel";


interface Truck {
  id: string;
  name: string;
  lat: number;
  lng: number;

  speed: number;
  fuelLevel: number;
  kmToService: number;

  engineTemperature: number;

  status: "active" | "idle" | "inactive";
}


interface VehicleDetailsPanelProps {
  selectedTruck: string | null;
  trucks: Truck[];

  onSelectTruck: (truckId: string | null) => void;

  detailsOpen: boolean;
  setDetailsOpen: (value: boolean) => void;
}



export default function VehicleDetailsPanel({

  selectedTruck,
  trucks,
  onSelectTruck,
  detailsOpen,
  setDetailsOpen,

}: VehicleDetailsPanelProps) {


  const selectedTruckData = trucks.find(
    (t) => t.id === selectedTruck
  );


  const panelRef = useRef<HTMLDivElement>(null);

  const [isMobile, setIsMobile] = useState(false);



  // MOBILE DETECTION
  useEffect(() => {

    const checkMobile = () => {
      setIsMobile(window.innerWidth < 768);
    };


    checkMobile();


    window.addEventListener(
      "resize",
      checkMobile
    );


    return () => {
      window.removeEventListener(
        "resize",
        checkMobile
      );
    };


  }, []);




  // CLICK OUTSIDE - ONLY MOBILE
  useEffect(() => {


    const handleClickOutside = (
      event: MouseEvent | TouchEvent
    ) => {


      if (!detailsOpen) return;


      const target = event.target as Node;


      if (panelRef.current?.contains(target)) {
        return;
      }


      if (isMobile) {
        setDetailsOpen(false);
        onSelectTruck(null);
      }


    };



    document.addEventListener(
      "mousedown",
      handleClickOutside
    );


    document.addEventListener(
      "touchstart",
      handleClickOutside
    );



    return () => {

      document.removeEventListener(
        "mousedown",
        handleClickOutside
      );


      document.removeEventListener(
        "touchstart",
        handleClickOutside
      );

    };


  }, [
    detailsOpen,
    isMobile,
    onSelectTruck,
    setDetailsOpen
  ]);





  return (

<div

  ref={panelRef}

  className={`
    fixed
    right-0
    top-0
    bottom-0

    w-80

    bg-gradient-to-b
    from-slate-900
    via-indigo-950
    to-slate-900

    border-l
    border-indigo-600/50

    flex
    flex-col

    shadow-2xl

    overflow-hidden

    z-[100]

    transition-transform
    duration-300


    ${detailsOpen
      ? "translate-x-0"
      : "translate-x-full"
    }

  `}

>




{/* WORKSHOP */}

<div className="mt-4 mx-4 p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">


  <div className="flex justify-center items-center gap-2">


    <span className="text-lg">
      🔧
    </span>


    <h3 className="text-lg font-bold text-white uppercase">
      Warsztat
    </h3>


  </div>


</div>






{/* PANEL HEADER */}

<div className="flex items-center justify-between px-4 py-3 border-b border-indigo-600/30">


  <h2 className="text-lg font-bold text-white uppercase">
    
  </h2>



  <button

    onClick={() => {

      onSelectTruck(null);
      setDetailsOpen(false);

    }}

    className="text-slate-400 hover:text-white transition-colors"

  >

    <X className="w-5 h-5" />

  </button>



</div>








{/* TRUCK DETAILS */}


{selectedTruckData ? (


<div className="flex-1 overflow-y-auto p-4">


<div className="mb-6">



<h2 className="text-lg font-bold text-white mb-4">
  {selectedTruckData.name}
</h2>





{/* ID */}

<div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">


<p className="text-xs text-slate-400 uppercase font-semibold mb-2">
Vehicle ID
</p>


<p className="font-mono text-white text-sm">
{selectedTruckData.id}
</p>


</div>





<div className="space-y-0 mt-3">



{/* SPEED */}

<div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">


<div className="flex items-center gap-3">


<Zap className="w-5 h-5 text-orange-400" />


<div>

<p className="text-xs text-slate-400 uppercase font-semibold">
Speed
</p>


<p className="text-lg font-bold text-white">
{selectedTruckData.speed} km/h
</p>


</div>


</div>


</div>






{/* FUEL */}


<div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">


<div className="flex items-center gap-3">


<Droplet className="w-5 h-5 text-blue-400" />


<div>


<p className="text-xs text-slate-400 uppercase font-semibold">
Fuel
</p>


<p className="text-lg font-bold text-white">
{selectedTruckData.fuelLevel}%
</p>


</div>


</div>


</div>






{/* SERVICE */}


<div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/50">


<div className="flex items-center gap-3">


<Thermometer className="w-5 h-5 text-red-400" />


<div>


<p className="text-xs text-slate-400 uppercase font-semibold">
Następny serwis
</p>


<p className="text-lg font-bold text-white">
{selectedTruckData.kmToService.toFixed(2)} km
</p>


</div>


</div>


</div>




</div>
<TruckDetailsPanel

  truck={selectedTruckData}

  onBack={() => onSelectTruck(null)}

/>


</div>


</div>



) : (



<div className="flex-1 flex items-center justify-center">


<div className="text-center p-6">


<MapPin className="w-12 h-12 text-slate-500 mx-auto mb-3" />


<p className="text-slate-400 text-sm">
Kliknij na pojazd aby zobaczyć detale
</p>


</div>


</div>



)}










</div>


  );
}