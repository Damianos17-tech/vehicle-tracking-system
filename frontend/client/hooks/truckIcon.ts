import L from "leaflet";

export const getTruckIcon = () => {
  return L.divIcon({
    html: `
      <div class="truck-icon-inner">
			🚚
      </div>
    `,
    className: "truck-icon",
    iconSize: [40, 40],
    iconAnchor: [20, 40],
    popupAnchor: [0, -40],
  });
};