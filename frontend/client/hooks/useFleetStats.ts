import { useEffect, useState } from "react";

export interface FleetStats {
  total: number;
  active: number;
  stopped: number;
  broken: number;
  warnings: number;
  paused: number;
}

export function useFleetStats() {

  const [stats, setStats] = useState<FleetStats>({
    total: 0,
    active: 0,
    stopped: 0,
    broken: 0,
    warnings: 0,
	paused: 0,
  });


  const API = `${window.location.protocol}//${window.location.hostname}:8090`;


  const fetchStats = async () => {
    try {

      const res = await fetch(`${API}/fleet/stats`);
      const data = await res.json();

      setStats(data);

    } catch (err) {
      console.error("Stats fetch error:", err);
    }
  };


  useEffect(() => {

    fetchStats();

    const interval = setInterval(() => {
      fetchStats();
    }, 2000);


    return () => clearInterval(interval);

  }, []);


  return stats;
}