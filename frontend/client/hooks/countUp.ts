import { useEffect, useRef, useState } from "react";

export function useCountUp(value: number, duration = 600) {
  const [display, setDisplay] = useState(value);
  const firstRender = useRef(true);

  useEffect(() => {
    // 👉 pierwsze wejście = animacja
    if (firstRender.current) {
      firstRender.current = false;

      const start = 0;
      const startTime = performance.now();

      const animate = (now: number) => {
        const progress = Math.min((now - startTime) / duration, 1);

        const current = Math.floor(start + (value - start) * progress);

        setDisplay(current);

        if (progress < 1) {
          requestAnimationFrame(animate);
        }
      };

      requestAnimationFrame(animate);
      return;
    }

    // 👉 kolejne zmiany = bez animacji
    setDisplay(value);

  }, [value]);

  return display;
}