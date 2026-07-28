import React from "react";

type Props = {
  truck: any;
  onBack: () => void;
};

export default function TruckDetailsPanel({ truck, onBack }: Props) {
  if (!truck) return null;

  //const API = "http://localhost:8080";
  const API = `${window.location.protocol}//${window.location.hostname}:8090`;
  

  const statusColor =
    truck.status === "ACTIVE"
      ? "#10b981"
      : truck.status === "WARNING"
      ? "#f59e0b"
      : truck.status === "FAILURE"
      ? "#ef4444"
      : "#94a3b8";

  const events = truck.events ?? [];
  const reversedEvents = [...events].reverse();

  const handleAction = async (action: string) => {
    try {
      await fetch(`${API}/fleet/truck/${truck.id}/${action}`, {
        method: "POST",
      });
    } catch (err) {
      console.error("Action error:", err);
    }
  };

  const repairAll = async () => {
    try {
      await fetch(`${API}/fleet/repair-all`, {
        method: "POST",
      });
    } catch (err) {
      console.error("Repair all error:", err);
    }
  };
  
  const refuel = async () => {
  try {
    const res = await fetch(`${API}/fleet/truck/${truck.id}/refuel`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Refuel failed");

    const updatedTruck = await res.json();

    // aktualizujesz UI danymi z backendu
    setTruck(updatedTruck);

  } catch (err) {
    console.error("Refuel error:", err);
  }
};


  const repair = async () => {
  try {
    const res = await fetch(`${API}/fleet/truck/${truck.id}/repair`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Repair failed");

    const updatedTruck = await res.json();

    // aktualizujesz UI danymi z backendu
    setTruck(updatedTruck);

  } catch (err) {
    console.error("Repair error:", err);
  }
};


  const service = async () => {
  try {
    const res = await fetch(`${API}/fleet/truck/${truck.id}/service`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Service failed");

    const updatedTruck = await res.json();

    // aktualizujesz UI danymi z backendu
    setTruck(updatedTruck);

  } catch (err) {
    console.error("Service error:", err);
  }
};
  

  return (
    <div className="flex flex-col gap-4 text-white">

      {/* BACK */}
      <button
        onClick={onBack}
        className="text-slate-200 hover:text-white"
      >
        ← Powrót
      </button>



		
      {/* HEADER */}
      <div className="text-md text-slate-200 flex items-center gap-4">
	  {/*
        <div
          style={{
            width: "12px",
            height: "12px",
            borderRadius: "50%",
            background: statusColor,
            animation: "pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite",
          }}
        />

		
        <div>
          [{truck.id}] <b>{truck.name}</b> ⛽{truck.fuelLevel}%
          <br />
          Total km: {truck.totalDistanceKm?.toFixed(2) ?? 0} | Next 🔧 in{" "}
          {truck.kmToService?.toFixed(0) ?? 0} km
        </div>
		*/}

      </div>
	  
	
      {/* ACTIONS */}
      <div className="grid grid-cols-2 gap-2 mt-2">

        <button
          onClick={() => handleAction("repair")}
          className="bg-red-600 hover:bg-red-500 rounded p-2"
        >
          Napraw
        </button>

        <button
          onClick={() => handleAction("service")}
          className="bg-yellow-600 hover:bg-yellow-500 rounded p-2"
        >
          Serwisuj
        </button>

        <button
          onClick={() => handleAction("refuel")}
          className="bg-blue-600 hover:bg-blue-500 rounded p-2"
        >
          Tankuj
        </button>

        <button
          onClick={repairAll}
          className="bg-green-600 hover:bg-green-500 rounded p-2"
        >
          Napraw wszystkie
        </button>
      </div>

      {/* EVENTS */}
      <div className="mt-3">
        <h3 className="text-sm font-bold text-slate-300 mb-2">
          Events
        </h3>

        <div className="space-y-2 max-h-48 overflow-y-auto pr-1">
          {reversedEvents.length > 0 ? (
            reversedEvents.map((event: any, index: number) => (
              <div
                key={index}
                className="p-2 rounded bg-slate-800/50 border border-slate-700"
              >
                <div className="flex justify-between">
                  <span className="text-xs font-bold text-white">
                    {event.type}
                  </span>

                  <span className="text-xs text-slate-400">
                    {event.createdAt}
                  </span>
                </div>

                <div className="text-xs text-slate-300 mt-1">
                  {event.message}
                </div>
              </div>
            ))
          ) : (
            <div className="text-xs text-slate-500">
              No events
            </div>
          )}
        </div>
      </div>

    </div>
  );
}