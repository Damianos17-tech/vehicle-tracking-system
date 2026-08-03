import { useState } from "react";

import MainHeader from "./MainHeader";
import "./InfrastructureDiagram.css";


interface InfrastructureDiagramProps {
  trucks: any[];
  onBack: () => void;
  messagesPerSecond: number;
}


export default function InfrastructureDiagram({
  trucks,
  onBack,
  messagesPerSecond
}: InfrastructureDiagramProps) {


  const [sidebarOpen, setSidebarOpen] = useState(false);


  const toggleSidebar = () => {
    setSidebarOpen(prev => !prev);
  };


  return (

    <div className="h-screen w-screen overflow-y-auto infrastructure-page">


			<MainHeader
			  trucks={trucks}
			  sidebarOpen={sidebarOpen}
			  toggleSidebar={toggleSidebar}
			  onHomeClick={onBack}
			  messagesPerSecond={messagesPerSecond}
			/>


      <div className="diagram-background">


        <h1>
          Platform Infrastructure
        </h1>


        <button
          onClick={onBack}
          className="back-button"
        >
          ← Back to Dashboard
        </button>



        <div className="architecture">


          {/* CORE PIPELINE */}


{/* SIMULATOR CLUSTER */}


<div className="simulators-row">


  <div className="node simulator">

    <h3>🚛 Simulator #1</h3>

    <p>
      Spring Boot Instance
    </p>

    <span>
      100 Trucks
    </span>

  </div>



  <div className="node simulator">

    <h3>🚛 Simulator #2</h3>

    <p>
      Spring Boot Instance
    </p>

    <span>
      100 Trucks
    </span>

  </div>



  <div className="node simulator">

    <h3>🚛 Simulator #3</h3>

    <p>
      Spring Boot Instance
    </p>

    <span>
      100 Trucks
    </span>

  </div>


</div>



<div className="arrow">
  ↓
</div>



          <div className="node kafka">

            <h3>📨 Apache Kafka</h3>

            <p>
              Message Broker
            </p>

            <span>
              truck-state
            </span>

            <span>
              truck-events
            </span>

            <span>
              truck-commands
            </span>

          </div>



          <div className="arrow">
            ↓
          </div>



          <div className="node backend">


            <h3>⚙️ Spring Boot Backend</h3>

            <p>
              Fleet Management API
            </p>

            <span>
              Kafka Consumer
            </span>

            <span>
              WebSocket Server
            </span>


          </div>




          {/* DATABASE LAYER */}


          <div className="infrastructure-row">


            <div className="node database">

              <h3>🐘 PostgreSQL</h3>

              <p>
                Vehicle Data
              </p>

            </div>



            <div className="node elastic">

              <h3>🔎 Elasticsearch</h3>

              <p>
                Events & Logs
              </p>

            </div>



            <div className="node osrm">

              <h3>🗺️ OSRM</h3>

              <p>
                Route Calculation
              </p>

            </div>


          </div>




          <div className="arrow">
            ↓
          </div>




          {/* FRONTEND */}


          <div className="node frontend">


            <h3>🌐 React Frontend</h3>

            <p>
              Fleet Operations Center
            </p>

            <span>
              WebSocket / STOMP
            </span>

            <span>
              Leaflet Map
            </span>


          </div>






          {/* MONITORING */}


          <div className="architecture-section">


            <h2>
              INFRASTRUKTURA / MONITORING
            </h2>



            <div className="infrastructure-row">


              <div className="node grafana">

                <h3>📊 Grafana</h3>

                <p>
                  :3000
                </p>

                <span>
                  Metrics Dashboard
                </span>

              </div>




              <div className="node kibana">

                <h3>🔎 Kibana</h3>

                <p>
                  :5601
                </p>

                <span>
                  Logs Visualization
                </span>

              </div>


            </div>





            <div className="infrastructure-row">


              <div className="node kafka-ui">


                <h3>📨 Kafka UI</h3>

                <p>
                  :7000
                </p>

                <span>
                  Topics / Messages
                </span>


              </div>


            </div>



          </div>






          {/* ROUTING */}



          <div className="architecture-section">


            <h2>
              ROUTING / MAPY
            </h2>



            <div className="infrastructure-row">


              <div className="node osrm">


                <h3>🗺️ OSRM</h3>


                <p>
                  :5000
                </p>


                <span>
                  Routing Mazowieckie
                </span>


              </div>


            </div>



            <div className="arrow">
              ↑
            </div>



            <div className="node backend">


              <h3>⚙️ Spring Backend</h3>

              <p>
                Route Service
              </p>


            </div>


          </div>



        </div>


      </div>


    </div>

  );

}