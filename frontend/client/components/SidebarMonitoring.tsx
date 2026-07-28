import { Activity, MapPin } from "lucide-react";

const monitoringItems = [
  {
    title: "Grafana",
    subtitle: "Metrics & Dashboards",
    icon: Activity,
    color: "orange",
  },
  {
    title: "Log Viewer",
    subtitle: "System Logs & Events",
    icon: MapPin,
    color: "green",
  },
];

function MonitoringItem({ title, subtitle, icon: Icon, color }: any) {
  const colorMap: any = {
    orange: "border-orange-500/50 bg-orange-900/40 text-orange-400",
    green: "border-green-500/50 bg-green-900/40 text-green-400",
  };

  return (
    <button className="w-full p-4 rounded-lg bg-slate-800/40 border border-slate-700/50 hover:bg-slate-700/60 transition-all text-left backdrop-blur-sm">
      <div className="flex items-center gap-3">
        <div className={`p-3 rounded-lg border ${colorMap[color]}`}>
          <Icon className="w-6 h-6" />
        </div>

        <div>
          <h3 className="font-semibold text-slate-100">{title}</h3>
          <p className="text-sm text-slate-400">{subtitle}</p>
        </div>
      </div>
    </button>
  );
}

export default function SidebarMonitoringSection() {
  return (
    <div>
      <h2 className="text-lg font-bold text-white mb-4 bg-gradient-to-r from-indigo-400 to-indigo-600 bg-clip-text text-transparent">
        Monitoring
      </h2>

      <div className="space-y-3">
        {monitoringItems.map((item, i) => (
          <MonitoringItem key={i} {...item} />
        ))}
      </div>
    </div>
  );
}