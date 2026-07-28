import L from "leaflet";

const activeAnimations = new WeakMap<L.Marker, number>();

export function animateMarker(
  marker: L.Marker,
  from: L.LatLng,
  to: L.LatLng,
  duration = 700
) {
  if (from.lat === to.lat && from.lng === to.lng) return;

  const prev = activeAnimations.get(marker);
  if (prev) cancelAnimationFrame(prev);

  const start = performance.now();

  const step = (time: number) => {
    const t = Math.min((time - start) / duration, 1);

    const lat = from.lat + (to.lat - from.lat) * t;
    const lng = from.lng + (to.lng - from.lng) * t;

    marker.setLatLng([lat, lng]);

    if (t < 1) {
      const id = requestAnimationFrame(step);
      activeAnimations.set(marker, id);
    } else {
      activeAnimations.delete(marker);
    }
  };

  const id = requestAnimationFrame(step);
  activeAnimations.set(marker, id);
}