const API = `${window.location.protocol}//${window.location.hostname}:8090`;

export async function repairAll() {
  await fetch(`${API}/fleet/repair-all`, {
    method: "POST",
  });
}

export async function truckAction(id: string, action: string) {
  await fetch(`${API}/fleet/truck/${id}/${action}`, {
    method: "POST",
  });
}