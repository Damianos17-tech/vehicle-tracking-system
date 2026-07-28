import React, { useEffect, useState } from "react";
import { Search } from "lucide-react";
//import { useCountUp } from "../hooks/CountUp";

import { useFleetStats } from "../hooks/useFleetStats";

const API = `${window.location.protocol}//${window.location.hostname}:8090`;

type Truck = {
  id: string;
};

type FleetStats = {
  total: number;
  broken: number;
  warnings: number;
};

interface Props {
 
  searchQuery: string;
  setSearchQuery: (v: string) => void;
}

export default function TopStatusBar({
  trucks,
  searchQuery,
  setSearchQuery,
}: Props) {

  // 🟢 STATE NA STATYSTYKI Z BACKENDU
  const [stats, setStats] = useState<FleetStats>({
    total: 0,
    broken: 0,
    warnings: 0,
  });

  // 🔵 FETCH STATS Z BACKENDU
  const fetchStats = async () => {
    try {
      const res = await fetch(`${API}/fleet/stats`);
      const data = await res.json();
	  
	  //console.log("STATS FROM BACKEND:", data);
	  
	  
      setStats(data);
    } catch (err) {
      console.error("Stats fetch error:", err);
    }
  };

  // 🚀 USEEFFECT (START + REFRESH CO 2s)
  useEffect(() => {
    fetchStats();

    const interval = setInterval(() => {
      fetchStats();
    }, 2000);

    return () => clearInterval(interval);
  }, []);
  
  const animationDuration = 2800;
  //const brokenCount = useCountUp(stats.broken, animationDuration);
  //const totalCount = useCountUp(stats.total, animationDuration);
  //const warningsCount = useCountUp(stats.warnings, animationDuration);
  //const alerts = useCountUp((stats.warnings+stats.broken), animationDuration);
  
  //console.log("STATS FROM count:", totalCount);
  
  const isHealthy = stats.total === 0 ? true : stats.broken / stats.total <= 0.05;

  return (
    <div className="border-b border-indigo-600 border-opacity-30 px-4 py-3 text-sm w-full">
      <div className="max-w-7xl mx-auto flex items-center justify-between gap-4">

        {/* STATUS LEFT */}
        <div className="flex items-center gap-4">

          <div className="flex items-center gap-2">
            <div
              className={`w-2 h-2 rounded-full animate-pulse ${
                isHealthy  ? "bg-emerald-400" : "bg-red-400"
              }`}
            />
			<span
			  className={`font-semibold ${
				isHealthy ? "text-emerald-400" : "text-red-400"
			  }`}
			>
			  {isHealthy ? "Healthy" : `${stats.broken} Incydentów`}
			</span>
          </div>

          <span className="text-slate-400">•</span>

          <span className="text-slate-300">
            {stats.total} pojazdów online
          </span>

          <span className="text-slate-400">•</span>

          <div className="flex items-center gap-1.5 text-amber-400">
            <span>🔔</span>
            <span>alerty: {stats.broken + stats.warnings}</span>
          </div>

        </div>

        {/* SEARCH RIGHT */}
        <div className="relative flex-1 max-w-xs md:mr-16">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-300" />

			<input
			  type="text"
			  placeholder="Search vehicles..."
			  value={searchQuery}
			  onChange={(e) => setSearchQuery(e.target.value)}
			  className="w-full bg-slate-800 border border-slate-600 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-100 placeholder-slate-400 focus:outline-none focus:border-indigo-400 focus:bg-slate-800 transition-all"
			/>
        </div>

      </div>
    </div>
  );
}