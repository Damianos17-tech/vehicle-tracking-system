//import { getStatusLabel } from "../utils/getStatusLabel";

export function createTruckPopup(truck: any) {
	
	
	
	
const statusColor =
  truck.status === "ACTIVE"
    ? "#10b981"
    : truck.status === "WARNING"
    ? "#f59e0b"
    : truck.status === "FAILURE"
    ? "#ef4444"
    : "#94a3b8";
	  
const getStatusLabel = (status: string) => {
  switch (status) {
    case "ACTIVE":
      return "Active";

    case "WARNING":
      return "Warning";

    case "FAILURE":
      return "Failure";

    case "INACTIVE":
      return "Inactive";

    default:
      return status;
  }
};



  return `
    <div>
      <h3 style="font-weight: bold; font-size: 18px; color: #1f2937; margin-bottom: 12px;">
        ${truck.name}
      </h3>

      <div style="display: flex; flex-direction: column; gap: 12px;">

        <!-- ID -->
        <div style="display: flex; gap: 12px; padding: 12px; background: #f8fafc; border-radius: 8px;">
          <div style="flex-shrink: 0;">📍</div>
          <div>
            <p style="font-size: 11px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 4px;">
              Vehicle ID
            </p>
            <p style="font-family: monospace; font-size: 14px; font-weight: bold; color: #1f2937;">
              ${truck.id}
            </p>
          </div>
        </div>

        <!-- SPEED -->
        <div style="display: flex; gap: 12px; padding: 12px; background: #f8fafc; border-radius: 8px;">
          <div style="flex-shrink: 0;">⚡</div>
          <div>
            <p style="font-size: 19px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 4px;">
              Speed
            </p>
            <p style="font-family: monospace; font-size: 21px; font-weight: bold; color: #1f2937;">
              ${truck.speed} km/h
            </p>
          </div>
        </div>

        <!-- STATUS -->
        <div style="padding: 12px; background: #f8fafc; border-radius: 8px; text-align: center;">
          <p style="font-size: 19px; font-weight: 600; color: #64748b; text-transform: uppercase; margin-bottom: 8px;">
            Status
          </p>

          <div style="display: flex; justify-content: center; align-items: center; gap: 8px;">
            <div style="width: 18px; height: 18px; border-radius: 50%; background: ${statusColor}; animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;"></div>

            <span style="font-weight: bold; font-size: 19px; color: #1f2937;">
              ${getStatusLabel(truck.status)}
            </span>
          </div>
        </div>

      </div>
    </div>
  `;
}