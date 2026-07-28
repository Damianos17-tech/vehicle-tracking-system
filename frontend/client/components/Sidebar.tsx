import React, { useEffect, useRef } from "react";
import { getStatusColor, getStatusLabel } from "../hooks/truckModel";
import TruckDetailsPanel from "./TruckDetailsPanel";
import { TruckCard, TruckCard2 } from "./TruckCard";

import {
  ChevronRight,
  Map as MapIcon,
  Activity,
  Settings,
  Shield,
} from "lucide-react";

import {
  sidebarBase,
  sidebarOpen as sidebarOpenClass,
  sidebarClosed,
  sidebarIndigo,
  sidebarSlate,
} from "../hooks/sidebarStyles";

import SidebarSecuritySection from "./SidebarSecurity";
import SidebarDevOpsSection from "./SidebarDevops";
import SidebarMonitoringSection from "./SidebarMonitoring";

type SectionType = "mapa" | "monitoring" | "devops" | "security";
type MapSubView = "mapa" | "pojazdy";

const sections: {
  id: SectionType;
  label: string;
  icon: React.ReactNode;
}[] = [
  { id: "mapa", label: "Zarządzanie flotą", icon: <MapIcon className="w-5 h-5" /> },
  { id: "monitoring", label: "Monitoring", icon: <Activity className="w-5 h-5" /> },
  { id: "devops", label: "DevOps", icon: <Settings className="w-5 h-5" /> },
  { id: "security", label: "Security", icon: <Shield className="w-5 h-5" /> },
];

export default function Sidebar({
  sidebarOpen,
  activeSection,
  setActiveSection,
  mapSubView,
  setMapSubView,
  trucks,
  selectedTruck,
  setSelectedTruck,
  setSidebarOpen,
  setDetailsOpen,
  handleSidebarInteraction,
  sidebarRef,
}: any) {

  // 🔥 REFY DO AUTO-SCROLLA
  const itemRefs = useRef(new Map<string, HTMLDivElement>());

  // 🔥 AUTO-SCROLL DO AKTYWNEGO TRUCKA
useEffect(() => {
  if (!selectedTruck) return;

  const id = selectedTruck;

  const timeout = setTimeout(() => {
    const el = itemRefs.current.get(id);

    if (el) {
      el.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    }
  }, 50);

  return () => clearTimeout(timeout);
}, [selectedTruck, mapSubView]);

  return (
    <div
      ref={sidebarRef}
      className={`${sidebarBase} ${
        sidebarOpen ? sidebarOpenClass : sidebarClosed
      }`}
    >
      {/* SECTION NAVIGATION */}
      <div className="flex flex-col gap-2 p-4 border-b border-indigo-600 border-opacity-30">
        {sections.map((section) => (
          <button
            key={section.id}
            onClick={() => setActiveSection(section.id)}
            className={`flex items-center justify-between w-full px-4 py-3 rounded-lg transition-all backdrop-blur-sm ${
              activeSection === section.id
                ? sidebarIndigo
                : sidebarSlate
            }`}
          >
            <div className="flex items-center gap-3">
              {section.icon}
              <span className="font-semibold">{section.label}</span>
            </div>

            {activeSection === section.id && (
              <ChevronRight className="w-5 h-5" />
            )}
          </button>
        ))}
      </div>

      {/* CONTENT */}
      <div className="flex-1 overflow-y-auto p-4 bg-gradient-to-b from-transparent via-indigo-900/10 to-transparent">

        {activeSection === "mapa" && (
          <>
            {/* SUBVIEW SWITCH */}
            <div className="flex gap-2 mb-4">
              <button
                onClick={() => setMapSubView("mapa")}
                className={`flex-1 px-3 py-2 rounded-lg font-semibold transition-all backdrop-blur-sm ${
                  mapSubView === "mapa"
                    ? sidebarIndigo
                    : sidebarSlate
                }`}
              >
                Mapa
              </button>


            </div>

            {/* MAP VIEW */}
            {mapSubView === "mapa" && (
              <div className="space-y-3">
                <h2 className="text-lg font-bold text-white mb-4 bg-gradient-to-r from-indigo-400 to-indigo-600 bg-clip-text text-transparent">
                  Fleet Overview
                </h2>

                {trucks.map((truck: any) => (
                  <div
                    key={truck.id}
                    ref={(el) => {
                      if (el) itemRefs.current.set(truck.id, el);
                    }}
                  >
<TruckCard
  truck={truck}
  selected={selectedTruck === truck.id}
  onClick={() => {
    setSelectedTruck(truck.id);
    setSidebarOpen(false);
    setDetailsOpen(true);
  }}
/>
                  </div>
                ))}
              </div>
            )}

            {/* POJAZDY VIEW */}

          </>
        )}

        {activeSection === "monitoring" && (
          <SidebarMonitoringSection />
        )}

        {activeSection === "devops" && (
          <SidebarDevOpsSection />
        )}

        {activeSection === "security" && (
          <SidebarSecuritySection />
        )}

      </div>
    </div>
  );
}