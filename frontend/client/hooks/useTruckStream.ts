import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";


export interface TruckEvent {
  type: string;
  message: string;
  createdAt: string;
  id: number;
  truckId: string;
}


export interface TruckData {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: "ACTIVE" | "WARNING" | "FAILURE" | "INACTIVE";
  name: string;

  warnings: string[];
  failures: string[];

  events: TruckEvent[];

  fuelLevel: number;
  technicalCondition: number;
  totalDistanceKm: number;
  kmToService: number;
}



export function useTruckStream() {


  const [trucks, setTrucks] = useState<TruckData[]>([]);

  const [messagesPerSecond, setMessagesPerSecond] = useState(0);


  const messageCounter = useRef(0);


  /*
      Pamięta kiedy ostatnio backend wysłał trucka
  */
  const lastSeen = useRef<Map<string, number>>(new Map());



  useEffect(() => {


    const client = new Client({


      webSocketFactory: () =>
        new WebSocket(
          `ws://${window.location.hostname}:8090/ws`
        ),


      reconnectDelay: 5000,

    });



    client.onConnect = () => {


      console.log("WS CONNECTED");



      client.subscribe(
        "/topic/trucks",
        (message) => {


          messageCounter.current++;


          const data = JSON.parse(
            message.body
          );



          const truckId = data.id;



          /*
              zapamiętaj że truck żyje
          */
          lastSeen.current.set(
            truckId,
            Date.now()
          );





          setTrucks((prev) => {


            const existingIndex =
              prev.findIndex(
                t => t.id === truckId
              );



            const oldTruck =
              existingIndex >= 0
                ? prev[existingIndex]
                : null;



            const updatedTruck: TruckData = {


              id: truckId,


              lat:
                data.position.latitude,


              lng:
                data.position.longitude,



              speed:
                data.speed,



              status:
                data.status,



              name:
                data.name ?? truckId,



              warnings:
                data.warnings ??
                oldTruck?.warnings ??
                [],



              failures:
                data.failures ??
                oldTruck?.failures ??
                [],



              events:
                data.events &&
                data.events.length > 0
                  ? data.events
                  : oldTruck?.events ?? [],



              fuelLevel:
                data.fuelLevel ??
                oldTruck?.fuelLevel ??
                0,



              technicalCondition:
                data.technicalCondition ??
                oldTruck?.technicalCondition ??
                0,



              totalDistanceKm:
                data.totalDistanceKm ??
                oldTruck?.totalDistanceKm ??
                0,



              kmToService:
                data.kmToService ??
                oldTruck?.kmToService ??
                0,

            };





            if(existingIndex === -1) {


              return [
                ...prev,
                updatedTruck
              ];


            }




            const copy = [...prev];


            copy[existingIndex] = updatedTruck;


            return copy;



          });



        }

      );


    };




    client.activate();




    /*
        CZYSZCZENIE STARYCH TRUCKÓW

        jeżeli backend przestanie wysyłać
        np. po release simulatora
        -> usuwamy z React state
    */
    const cleanupInterval =
      setInterval(() => {


        const now = Date.now();


        setTrucks(prev =>

          prev.filter(truck => {


            const seen =
              lastSeen.current.get(
                truck.id
              );



            if(!seen)
              return false;



            return (
              now - seen < 15000
            );

          })

        );


      },5000);






    return () => {


      clearInterval(
        cleanupInterval
      );


      client.deactivate();


    };



  }, []);






  useEffect(() => {


    const interval =
      setInterval(() => {


        setMessagesPerSecond(
          messageCounter.current
        );


        messageCounter.current = 0;


      },1000);



    return () =>
      clearInterval(interval);



  }, []);






  return {

    trucks,

    messagesPerSecond

  };


}