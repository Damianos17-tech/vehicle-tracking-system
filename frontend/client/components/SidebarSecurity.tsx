import { Activity, Shield, LucideIcon } from "lucide-react";

/**
 * 1. DATA (konfiguracja)
 */
const securityItems = [
  {
    title: "Testy Wydajnościowe",
    subtitle: "Performance & Load Testing",
    icon: Activity,
    color: "yellow",
  },
  {
    title: "Hacking Tests / Pentesting",
    subtitle: "Security Penetration Testing",
    icon: Shield,
    color: "red",
  },
  {
    title: "🔐 Certyfikaty i Audyt",
    subtitle: "Security Compliance",
    icon: Shield,
    color: "green",
  },
];

/**
 * 2. COMPONENT (renderer)
 */
type ItemProps = {
  title: string;
  subtitle: string;
  Icon: LucideIcon;
  color: string;
};

function SecurityItem({ title, subtitle, Icon, color }: ItemProps) {
  const colorMap: Record<string, string> = {
    yellow: "border-yellow-500/50 bg-yellow-900/40 text-yellow-400",
    red: "border-red-500/50 bg-red-900/40 text-red-400",
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

/**
 * 3. MAIN SECTION (do użycia w Sidebar)
 */
export default function SidebarSecuritySection() {
  return (
    <div>
      <h2 className="text-lg font-bold text-white mb-4 bg-gradient-to-r from-indigo-400 to-indigo-600 bg-clip-text text-transparent">
        Security
      </h2>

      <div className="space-y-3">
        {securityItems.map((item, i) => (
          <SecurityItem
            key={i}
            title={item.title}
            subtitle={item.subtitle}
            Icon={item.icon}
            color={item.color}
          />
        ))}
      </div>
    </div>
  );
}