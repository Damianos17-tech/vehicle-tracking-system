import { Zap, Cpu, HardDrive, Truck, AlertCircle, Map, BarChart3, FileText, Wrench, Settings, Cloud, Network } from 'lucide-react';
import { useEffect, useRef, useState } from "react";
import Sidebar from "./Sidebar";
import MainHeader from "./MainHeader";
import TopStatusBar from "./TopStatusBar";

//import { useTruckStream } from "../hooks/useTruckStream";
import { useFleetStats } from "../hooks/useFleetStats";
import { useInfrastructureStats, formatUptime } from "../hooks/useInfrastructureStats";

import { repairAll } from "../hooks/fleetApi";

interface FleetOperationCenterProps {
  onViewSelect: (view: string) => void;
  activeView: string;
  trucks: any[];
  messagesPerSecond: number;
}

  
  

export default function FleetOperationCenter({ onViewSelect, activeView, trucks, messagesPerSecond }: FleetOperationCenterProps) {
	
	
	
  const HOST = window.location.hostname;

  const KIBANA_URL = `http://${HOST}:5601`;
  const GRAFANA_URL = `http://${HOST}:3000`;
	
	

  const [selectedTruck, setSelectedTruck] = useState<string | null>(null);
  const [activeSection, setActiveSection] = useState<SectionType>("mapa");
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [mapSubView, setMapSubView] = useState<"mapa" | "pojazdy">("mapa");
  const [searchQuery, setSearchQuery] = useState("");
  const sidebarRef = useRef<HTMLDivElement>(null);
	
	const stats = useFleetStats();
	
	const infra = useInfrastructureStats();
	
	//const { truckss, messagesPerSecond } = useTruckStream();
	
	const toggleSidebar = () => {
  setSidebarOpen(prev => !prev);
};
	
	const secondsInMinute = infra.uptimeSeconds % 60;
	const uptimeProgress =((infra.uptimeSeconds % 60) / 3600) * 100;
	
	const getColor = (value:number) => {

  if(value >= 80) return "bg-red-500";
  if(value >= 60) return "bg-yellow-500";
  return "bg-green-500";

}
	
	
	
	
	
  return (
    <div className="h-screen w-screen overflow-y-auto bg-gradient-to-b from-slate-900 via-indigo-950 to-slate-900 p-6 shadow-lg">
      <div className="max-w-7xl mx-auto">
	  
	  
	  
	  {/*
	          <TopStatusBar
          trucks={trucks}
          searchQuery={searchQuery}
          setSearchQuery={setSearchQuery}
        />
		
	  */}

			<MainHeader
			  trucks={trucks}
			  sidebarOpen={sidebarOpen}
			  toggleSidebar={toggleSidebar}
			  onHomeClick={() => onViewSelect("dashboard")}
			  messagesPerSecond={messagesPerSecond}
			/>
				 

	  

        {/* Fleet Operation Center Header */}
		{/* Fleet Operation Center Header */}
		<div className="flex items-center gap-3 w-full">
		  <div className="h-px bg-slate-700 flex-1" />

		  <h2 className="flex items-center gap-2 text-2xl font-bold text-white pb-10 pt-2">
			<Zap className="w-6 h-6 text-indigo-400" />
			Fleet Operation Center
		  </h2>

		  <div className="h-px bg-slate-700 flex-1" />
		</div>

        {/* Status Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          {/* Infrastructure */}
          <div className="p-5 rounded-lg bg-slate-800/40 border border-slate-100/50 backdrop-blur-sm">
            <div className="flex items-center gap-3 mb-4">
              <Cpu className="w-6 h-6 text-emerald-400" />
              <h3 className="font-semibold text-white">Infrastruktura</h3>
            </div>
            <div className="space-y-3">
			
			
													<div className="flex items-center justify-between">
													  <span className="text-sm text-slate-200">Status</span>

													  <span className="flex items-center gap-1.5">

														<div
														  className={`w-2 h-2 rounded-full ${
															infra.connected
															  ? "bg-emerald-400 animate-pulse"
															  : "bg-slate-500"
														  }`}
														></div>


														<span
														  className={`text-sm font-bold ${
															infra.connected
															  ? "text-emerald-400"
															  : "text-slate-400"
														  }`}
														>
														  {infra.connected
															? "Healthy, Connected"
															: "Disconnected from backend"}
														</span>

													  </span>
													</div>
			  
			  
			  
			  
              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-md text-slate-200">CPU</span>
                  <span className="text-md font-bold text-white">{infra.cpu}%</span>
                </div>
				<div className="relative w-full bg-slate-700 rounded-full h-4 overflow-hidden">

				  <div
					className={`${getColor(infra.cpu)} h-4 rounded-full transition-all`}
					style={{ width: `${infra.cpu}%` }}
				  ></div>


				  <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white">
					{infra.cpu}%
				  </span>

				</div>
              </div>
			  
			  
              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-md text-slate-200">JVM Memory Usage</span>
                  <span className="text-md font-bold text-white">{infra.jvmMemoryUsedMB}MB / {infra.jvmMemoryMaxMB}MB</span>
                </div>
				<div className="relative w-full bg-slate-700 rounded-full h-4 overflow-hidden">

				  <div
					className={`${getColor(infra.memoryPercent)} h-4 rounded-full transition-all`}
					style={{ width: `${infra.memoryPercent}%` }}
				  />

				  <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white">
					{infra.memoryPercent}%
				  </span>

				</div>
              </div>
			  
			  <div>
                <div className="flex justify-between mb-1">
                  <span className="text-md text-slate-200">Disk Usage </span>
                  <span className="text-md font-bold text-white">{(infra.diskFree / 1000).toFixed(1)} GB / {(infra.diskTotal / 1000).toFixed(1)} GB</span>
                </div>
				<div className="relative w-full bg-slate-700 rounded-full h-4 overflow-hidden">

				  <div
					className={`${getColor(infra.diskPercent)} h-4 rounded-full transition-all`}
					style={{ width: `${infra.diskPercent}%` }}
				  />

				  <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white">
					{infra.diskPercent}%
				  </span>

				</div>
              </div>
			  
			  
			  <div>
                <div className="flex justify-between mb-1">
                  <span className="text-md text-slate-200">Uptime</span>
                  <span className="text-md font-bold text-white">{infra.uptimeFormatted}</span>
                </div>
                <div className="w-full bg-slate-700 rounded-full h-2">
                  <div className="bg-blue-400 h-2 rounded-full" style={{ width: `${uptimeProgress }%` }}></div>
                </div>
              </div>
			  
			  
			  
            </div>
          </div>

          {/* Fleet Status */}
          <div className="p-5 rounded-lg bg-slate-800/40 border border-slate-100/50 backdrop-blur-sm">
            <div className="flex items-center gap-3 mb-4">
              <Truck className="w-6 h-6 text-indigo-400" />
              <h3 className="font-semibold text-white">Status Floty</h3>
            </div>
            <div className="space-y-3">
              <div className="flex justify-between items-center p-2 bg-slate-700/30 rounded">
                <span className="text-md text-slate-200">Vehicles</span>
                <span className="text-2xl font-bold text-indigo-400">{stats.total}</span>
              </div>
              <div className="flex justify-between items-center p-2 bg-slate-700/30 rounded">
                <span className="text-md text-slate-200">Moving</span>
                <span className="text-2xl font-bold text-emerald-400">{stats.total-stats.stopped}</span>
              </div>
              <div className="flex justify-between items-center p-2 bg-slate-700/30 rounded">
                <span className="text-md text-slate-200">Stopped</span>
                <span className="text-2xl font-bold text-amber-400">{stats.stopped}</span>
              </div>
			  <div className="flex justify-center pt-2">
				<button
				  onClick={repairAll}
				  className="bg-green-600 hover:bg-green-500 rounded p-2 text-white">
				  Napraw wszystkie
				</button>
				</div>
            </div>
          </div>

          {/* Active Alerts */}
          <div className="p-5 rounded-lg bg-slate-800/40 border border-slate-100/50 backdrop-blur-sm">
		  
		  
            <div className="flex items-center gap-3 mb-4">
              <AlertCircle className="w-6 h-6 text-red-400" />
              <h3 className="font-semibold text-white">Aktywne Alerty</h3>
            </div>
			
            <div className="space-y-2">
			
			
			<div className="p-3 bg-red-900/20 border border-red-700/30 rounded flex justify-between items-center">
			  <div>
				<p className="text-sm text-red-400 font-semibold">Critical</p>
				<p className="text-xs text-red-300 mt-1">
				  Awarie + wypadki
				</p>
			  </div>
			  <div className="text-2xl font-bold text-red-400">
				{stats.broken}
			  </div>
			</div>
			
			
			  <div className="p-3 bg-yellow-900/20 border border-yellow-700/30 rounded flex justify-between items-center">
			  <div>
				<p className="text-sm text-yellow-400 font-semibold">Warning</p>
				<p className="text-xs text-yellow-300 mt-1">
				  Low fuel levels + Przekroczenia prędkości
				</p>
			  </div>

			  <div className="text-2xl font-bold text-yellow-400">
				{stats.warnings-stats.paused}
			  </div>
			</div>
						  
				<div className="p-3 bg-blue-900/20 border border-blue-700/30 rounded flex justify-between items-center">
				  <div>
					<p className="text-sm text-blue-400 font-semibold">Info</p>
					<p className="text-xs text-blue-300 mt-1">
					  Kierowcy na przerwie
					</p>
				  </div>

				  <div className="text-2xl font-bold text-blue-400">
					{stats.paused}
				  </div>
				</div>
			  
			  
			  
            </div>
			
			
          </div>
		  
		  
        </div>

        {/* View Selection Tiles */}
			<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
			  {[
				{ id: 'map', label: 'Mapa', icon: Map, hover: 'hover:from-green-600 hover:to-green-700' },
				{ id: 'monitoring', label: 'Monitoring', icon: BarChart3, hover: 'hover:from-orange-600 hover:to-orange-700' },
				{ id: 'logs', label: 'Logi', icon: FileText, hover: 'hover:from-blue-600 hover:to-blue-700' },
				{ id: 'cicd', label: 'CI/CD', icon: Wrench, hover: 'hover:from-purple-600 hover:to-purple-700' },
				{ id: 'infra', label: 'Infrastruktura', icon: Network, hover: 'hover:from-yellow-600 hover:to-yellow-700' },
				{ id: 'settings', label: 'Ustawienia', icon: Settings, hover: 'hover:from-slate-600 hover:to-slate-700' },
			  ].map((view) => {
				const IconComponent = view.icon;

				return (
				  <button
					key={view.id}
					onClick={() => {
					  if (view.id === "logs") {
						window.open(
						  `${KIBANA_URL}`,
						  "_blank"
						);
					  }
						else if (view.id === "monitoring") {
						window.open(
						  `${GRAFANA_URL}/dashboards?starred`,
						  "_blank"
						);
					  }

					  else {
						onViewSelect(view.id);
					  }
					}}
					className={`
					  group
					  p-4
					  aspect-square
					  rounded-lg
					  transition-all
					  transform
					  hover:scale-105

					  bg-slate-800/40
					  border border-slate-100/50
					  text-slate-300

					  hover:bg-gradient-to-br
					  ${view.hover}

					  hover:text-white
					  hover:border-white/20
					  hover:shadow-lg
					  hover:shadow-indigo-500/50
					`}
				  >
					<IconComponent 
					  className="
						w-20 h-20 
						mx-auto 
						mb-2
						transition-colors
						group-hover:text-white
					  "
					/>

					<p className="text-sm font-semibold">
					  {view.label}
					</p>

				  </button>
				);
			  })}
			</div>
		
		
		
		
		
		
      </div>
	  
	  
	  
	  <div className="mt-20 mb-10 text-center text-slate-200">
			<p className="text-sm">
			© 2026 FleetOps Tracker Platform
		  </p>

		  <p className="text-xs mt-1 text-slate-300">
			Designed & Engineered by Damianos with AI assistance
		  </p>
		</div>
	  
	  
    </div>
  );
}
