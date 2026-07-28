import { useEffect, useState } from "react";

export interface InfrastructureStats {

  status: string;
  cpu: number;
  hostRam: number;

  jvmMemoryUsedMB: number;
  jvmMemoryMaxMB: number;
  memoryPercent: number;

  diskTotal: number;
  diskFree: number;
  diskPercent: number;

  uptimeSeconds: number;
  uptimeFormatted: string;

  connected: boolean;

}



export function formatUptime(seconds: number) {

  const days = Math.floor(seconds / 86400);

  const hours = Math.floor((seconds % 86400) / 3600);

  const minutes = Math.floor((seconds % 3600) / 60);

  const secs = Math.floor(seconds % 60);


  return `${days}d ${String(hours).padStart(2, "0")}:${String(minutes).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;

}




export function useInfrastructureStats() {


  const [stats, setStats] = useState<InfrastructureStats>({

    status: "Disconnected from backend",

    cpu: 0,

    hostRam: 0,

    jvmMemoryUsedMB: 0,

    jvmMemoryMaxMB: 0,

    memoryPercent: 0,

    diskTotal: 0,

    diskFree: 0,

    diskPercent: 0,

    uptimeSeconds: 0,

    uptimeFormatted: "0d 00:00:00",

    connected: false

  });



  const API =
    `${window.location.protocol}//${window.location.hostname}:8090`;





  const fetchStats = async () => {


    const controller = new AbortController();


    const timeout = setTimeout(() => {

      controller.abort();

    }, 3000);



    try {


      const res = await fetch(
        `${API}/system/stats`,
        {
          signal: controller.signal
        }
      );



      clearTimeout(timeout);



      if (!res.ok) {

        throw new Error(
          "Backend returned error"
        );

      }



      const data = await res.json();



      //console.log("INFRA BACKEND OK:",data);



      setStats({

        ...data,

        connected: true,

        uptimeFormatted:
          formatUptime(data.uptimeSeconds)

      });



    } catch(err) {



      clearTimeout(timeout);



      console.error(
        "INFRA BACKEND OFFLINE:",
        err
      );



      setStats(prev => ({

        ...prev,

        connected: false,

        status: "Disconnected from backend"

      }));


    }


  };






  useEffect(() => {


    fetchStats();



    const interval = setInterval(() => {

      fetchStats();

    }, 1000);



    return () => {

      clearInterval(interval);

    };


  }, []);






  return stats;


}