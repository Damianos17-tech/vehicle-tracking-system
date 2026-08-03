import { useEffect, useState } from "react";
import FleetOperationCenter from "@/components/FleetOperationCenter";
import TruckMap from "@/components/TruckMap";
import { useTruckStream } from "@/hooks/useTruckStream";
import InfrastructureDiagram from "@/components/InfrastructureDiagram";

export default function Index() {

  const [activeView, setActiveView] = useState("dashboard");
  const { trucks, messagesPerSecond } = useTruckStream();
//console.log("INDEX RENDER");


  const changeView = (view: string) => {

    window.history.pushState(
      { view },
      "",
      `#${view}`
    );

    setActiveView(view);
  };


  useEffect(() => {

    const handleBack = () => {

      const view = window.location.hash.replace("#", "");

      if (view) {
        setActiveView(view);
      } else {
        setActiveView("dashboard");
      }

    };


    window.addEventListener(
      "popstate",
      handleBack
    );


    return () => {
      window.removeEventListener(
        "popstate",
        handleBack
      );
    };

  }, []);



  return (
    <>
      {activeView === "dashboard" && (
		<FleetOperationCenter
		  activeView={activeView}
		  onViewSelect={changeView}
		  trucks={trucks}
		  messagesPerSecond={messagesPerSecond}
		/>
      )}


      {activeView === "map" && (
					<TruckMap
					  onBack={() => changeView("dashboard")}
					  trucks={trucks}
					  messagesPerSecond={messagesPerSecond}
					/>
      )}
	  
	  
				{activeView === "infra" && (
				  <InfrastructureDiagram
					trucks={trucks}
					onBack={() => changeView("dashboard")}
				  />
				)}
				  
	  
    </>
  );
}