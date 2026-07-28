import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";

export interface TruckData {
  id: string;
  lat: number;
  lng: number;
  speed: number;
  status: "ACTIVE" | "WARNING" | "FAILURE" | "INACTIVE";
  name: string;
  warnings: string[];
  failures: string[];
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
        //new WebSocket("ws://localhost:8090/ws"),
        new WebSocket(`ws://${window.location.hostname}:8090/ws`),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      console.log("WS CONNECTED");

		client.subscribe("/topic/trucks", (message) => {

		  messageCounter.current++;
		  const data = JSON.parse(message.body);
		

		//console.log("TRUCK FROM BACKEND:", data);
		//console.log(data.id, data.status);

        setTrucks((prev) => {
          const existingIndex = prev.findIndex(t => t.id === data.id);

			const updatedTruck: TruckData = {
			  id: data.id,
			  lat: data.position.latitude,
			  lng: data.position.longitude,

			  speed: data.speed,
			  status: data.status,

			  name: data.name ?? data.id,

			  warnings: data.warnings ?? [],
			  failures: data.failures ?? [],
			  events: data.events ?? [],

			  fuelLevel: data.fuelLevel ?? 0,
			  technicalCondition: data.technicalCondition ?? 0,

			  totalDistanceKm: data.totalDistanceKm ?? 0,
			  kmToService: data.kmToService ?? 0,
			};

          if (existingIndex === -1) {
            return [...prev, updatedTruck];
          }

          const copy = [...prev];
          copy[existingIndex] = updatedTruck;

          return copy;
        });
      });
    };

    client.onStompError = (frame) => {
      console.error("STOMP ERROR:", frame.headers["message"]);
    };

    client.activate();
    return () => client.deactivate();
  }, []);
  
  //console.log("TRUCK FROM BACKEND:", trucks);
  
		  useEffect(() => {
		  const interval = setInterval(() => {

			setMessagesPerSecond(messageCounter.current);

			messageCounter.current = 0;

		  }, 1000);

		  return () => clearInterval(interval);

		}, []);
		  
  //

  return {
  trucks,
  messagesPerSecond
};
}