import { getStatusColor, getStatusLabel } from "../hooks/truckModel";

export function TruckCard({
  truck,
  selected,
  onClick,
}: {
  truck: any;
  selected?: boolean;
  onClick: () => void;
}) {
  return (
    <div
      onClick={onClick}
      className={`p-4 rounded-lg cursor-pointer transition-all backdrop-blur-sm ${
        selected
          ? "bg-gradient-to-r from-indigo-600/60 to-indigo-700/60 border-2 border-indigo-500 shadow-lg shadow-indigo-500/50 text-white"
          : "bg-slate-800/40 border border-slate-700/50 hover:border-indigo-500/50 text-slate-300"
      }`}
    >
      <div className="flex items-start justify-between mb-2">
        <h3 className="font-semibold">{truck.name}</h3>

        <div
          className={`px-2 py-1 rounded text-xs font-bold text-white ${getStatusColor(
            truck.status
          )}`}
        >
          {getStatusLabel(truck.status)}
        </div>
      </div>

      <div className="flex justify-between text-sm">
        <span>ID: {truck.id}</span>
        <span className="font-mono font-bold">{truck.speed} km/h</span>
      </div>
    </div>
  );
}






export function TruckCard2({
  truck,
  selected,
  onClick,
}: {
  truck: any;
  selected?: boolean;
  onClick: () => void;
}) {
  return (
    <div
      onClick={onClick}
      className={`p-4 rounded-lg cursor-pointer transition-all backdrop-blur-sm ${
        selected
          ? "bg-gradient-to-r from-indigo-600/60 to-indigo-700/60 border-2 border-indigo-500 shadow-lg shadow-indigo-500/50 text-white"
          : "bg-slate-800/40 border border-slate-700/50 hover:border-indigo-500/50 text-slate-300"
      }`}
    >
      <div className="flex items-start justify-between mb-2">
        <h3 className="font-semibold">{truck.name}</h3>

        <div
          className={`px-2 py-1 rounded text-xs font-bold text-white ${getStatusColor(
            truck.status
          )}`}
        >
          {getStatusLabel(truck.status)}
        </div>
      </div>

      <div className="flex justify-between text-sm">
        <span>ID: {truck.id}</span>
        <span className=""> 🛠🔧  : {truck.technicalCondition.toFixed(1)} %  </span>
      </div>
    </div>
  );
}