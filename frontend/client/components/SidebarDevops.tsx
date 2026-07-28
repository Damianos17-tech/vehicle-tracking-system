import { Zap, Settings, LucideIcon } from "lucide-react";

const devopsItems = [
  {
    title: "CI/CD",
    subtitle: "Pipeline & Deployments",
    icon: Zap,
    color: "purple",
  },
  {
    title: "Ustawienia",
    subtitle: "System Configuration",
    icon: Settings,
    color: "blue",
  },
];

function DevOpsItem({ title, subtitle, icon: Icon, color }: any) {
  const colorMap: any = {
    purple: "border-purple-500/50 bg-purple-900/40 text-purple-400",
    blue: "border-blue-500/50 bg-blue-900/40 text-blue-400",
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

export default function SidebarDevOpsSection() {
  return (
    <div>
      <h2 className="text-lg font-bold text-white mb-4 bg-gradient-to-r from-indigo-400 to-indigo-600 bg-clip-text text-transparent">
        DevOps
      </h2>

      <div className="space-y-3">
        {devopsItems.map((item, i) => (
          <DevOpsItem key={i} {...item} />
        ))}
      </div>
    </div>
  );
}