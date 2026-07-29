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


  useEffect(() => {

    const client = new Client({

      webSocketFactory: () =>
        new WebSocket(`ws://${window.location.hostname}:8090/ws`),

      reconnectDelay: 5000,
    });


    client.onConnect = () => {

      console.log("WS CONNECTED");


      client.subscribe("/topic/trucks", (message) => {

        messageCounter.current++;

        const data = JSON.parse(message.body);


        setTrucks((prev) => {

          const existingIndex = prev.findIndex(
            t => t.id === data.id
          );


          const oldTruck = 
            existingIndex >= 0 
              ? prev[existingIndex]
              : null;


          const updatedTruck: TruckData = {

            id: data.id,

            lat: data.position.latitude,
            lng: data.position.longitude,

            speed: data.speed,

            status: data.status,

            name: data.name ?? data.id,


            warnings: data.warnings ?? oldTruck?.warnings ?? [],

            failures: data.failures ?? oldTruck?.failures ?? [],


            events:
              data.events && data.events.length > 0
                ? data.events
                : oldTruck?.events ?? [],



            fuelLevel:
              data.fuelLevel ?? oldTruck?.fuelLevel ?? 0,


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



          if (existingIndex === -1) {

            return [
              ...prev,
              updatedTruck
            ];

          }


          const copy = [...prev];

          copy[existingIndex] = {
            ...oldTruck!,
            ...updatedTruck
          };


          return copy;

        });

      });

    };



    client.onStompError = (frame) => {

      console.error(
        "STOMP ERROR:",
        frame.headers["message"]
      );

    };


    client.activate();


    return () => {

      client.deactivate();

    };


  }, []);



  useEffect(() => {

    const interval = setInterval(() => {

      setMessagesPerSecond(
        messageCounter.current
      );

      messageCounter.current = 0;

    }, 1000);


    return () => clearInterval(interval);

  }, []);



  return {
    trucks,
    messagesPerSecond
  };

}