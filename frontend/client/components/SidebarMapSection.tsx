import L from "leaflet";
import { Map as MapIcon } from "lucide-react";

type MapSubView = "mapa" | "pojazdy";

type Truck = {
  id: string;
  name: string;
  lat: number;
  lng: number;
  speed: number;
  status: "active" | "idle" | "inactive";
};

interface Props {
  mapSubView: MapSubView;
  setMapSubView: (v: MapSubView) => void;

  trucks: Truck[];

  selectedTruck: string | null;
  setSelectedTruck: (v: string | null) => void;

  map: React.RefObject<L.Map | null>;
  markersRef: React.RefObject<{ [key: string]: L.Marker }>;

  getStatusColor: (s: string) => string;
  getStatusLabel: (s: string) => string;
}

export default function SidebarMapSection({
  mapSubView,
  setMapSubView,
  trucks,
  selectedTruck,
  setSelectedTruck,
  map,
  markersRef,
  getStatusColor,
  getStatusLabel,
}: Props) {
  return (
    <div>
      {/* TOGGLE */}
      <div className="flex gap-2 mb-4">
        <button
          onClick={() => setMapSubView("mapa")}
          className={`flex-1 px-3 py-2 rounded-lg font-semibold transition-all backdrop-blur-sm ${
            mapSubView === "mapa"
              ? "bg-gradient-to-r from-indigo-600 to-indigo-700 text-white shadow-lg shadow-indigo-500/70 border border-indigo-500/50"
              : "bg-slate-800 bg-opacity-40 text-slate-300 hover:bg-slate-700 hover:bg-opacity-60 border border-slate-700 border-opacity-30"
          }`}
        >
          Mapa
        </button>

        <button
          onClick={() => setMapSubView("pojazdy")}
          className={`flex-1 px-3 py-2 rounded-lg font-semibold transition-all backdrop-blur-sm ${
            mapSubView === "pojazdy"
              ? "bg-gradient-to-r from-indigo-600 to-indigo-700 text-white shadow-lg shadow-indigo-500/70 border border-indigo-500/50"
              : "bg-slate-800 bg-opacity-40 text-slate-300 hover:bg-slate-700 hover:bg-opacity-60 border border-slate-700 border-opacity-30"
          }`}
        >
          Pojazdy
        </button>
      </div>

      {/* FLEET VIEW */}
      {mapSubView === "pojazdy" && (
        <div>
          <h2 className="text-lg font-bold text-white mb-4 bg-gradient-to-r from-indigo-400 to-indigo-600 bg-clip-text text-transparent">
            Fleet Overview
          </h2>

          <div className="space-y-3">
            {trucks.map((truck) => (
              <div
                key={truck.id}
                onClick={() => {
                  setSelectedTruck(truck.id);

                  if (markersRef.current[truck.id]) {
                    const marker = markersRef.current[truck.id];
                    map.current?.setView(marker.getLatLng(), 13);
                    marker.openPopup();
                  }
                }}
                className={`p-4 rounded-lg cursor-pointer transition-all backdrop-blur-sm ${
                  selectedTruck === truck.id
                    ? "bg-gradient-to-r from-indigo-600/60 to-indigo-700/60 border-2 border-indigo-500 shadow-lg shadow-indigo-500/50 text-white"
                    : "bg-slate-800/40 border border-slate-700/50 hover:border-indigo-500/50 text-slate-300"
                }`}
              >
                <div className="flex items-start justify-between mb-2">
                  <h3
                    className={`font-semibold ${
                      selectedTruck === truck.id
                        ? "text-white"
                        : "text-slate-200"
                    }`}
                  >
                    {truck.name}
                  </h3>

                  <div
                    className={`px-2 py-1 rounded text-xs font-bold text-white ${getStatusColor(
                      truck.status
                    )}`}
                  >
                    {getStatusLabel(truck.status)}
                  </div>
                </div>

                <div
                  className={`flex justify-between text-sm ${
                    selectedTruck === truck.id
                      ? "text-indigo-100"
                      : "text-slate-400"
                  }`}
                >
                  <span>ID: {truck.id}</span>
                  <span className="font-mono font-bold">
                    {truck.speed} km/h
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* EMPTY MAP VIEW */}
      {mapSubView === "mapa" && (
        <div className="text-center py-8 text-slate-400">
          <MapIcon className="w-12 h-12 mx-auto mb-4 text-indigo-400" />
          <p>Mapa wyświetlana w głównym obszarze</p>
        </div>
      )}
    </div>
  );
}