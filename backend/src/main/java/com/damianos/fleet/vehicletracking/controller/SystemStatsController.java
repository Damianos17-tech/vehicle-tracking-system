package com.damianos.fleet.vehicletracking.controller;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemStatsController {


    private final MeterRegistry registry;


    public SystemStatsController(MeterRegistry registry) {
        this.registry = registry;
    }




    @GetMapping("/stats")
    public Map<String, Object> stats() {


        // CPU
        double cpu =
                getMetric("system.cpu.usage");



        // JVM HEAP MEMORY
        double memoryUsed =
                getHeapMetric("jvm.memory.used");


        double memoryMax =
                getHeapMetric("jvm.memory.max");



        double memoryPercent = 0;


        if(memoryMax > 0) {

            memoryPercent =
                    (memoryUsed / memoryMax) * 100;

        }





        // DISK

        double diskTotal =
                getMetric("disk.total");


        double diskFree =
                getMetric("disk.free");



        double diskPercent = 0;


        if(diskTotal > 0) {

            diskPercent =
                    ((diskTotal - diskFree)
                            /
                            diskTotal)
                            * 100;

        }





        // UPTIME

        double uptime =
                getMetric("process.uptime");





        String status =
                cpu < 0.90 &&
                        memoryPercent < 90

                        ?
                        "Healthy"

                        :
                        "Warning";






        return Map.of(

                "status",
                status,


                "cpu",
                Math.round(cpu * 100),



                "jvmMemoryUsedMB",
                Math.round(memoryUsed / 1024 / 1024),



                "jvmMemoryMaxMB",
                Math.round(memoryMax / 1024 / 1024),



                "memoryPercent",
                Math.round(memoryPercent),




                "diskFree",
                Math.round(diskFree / 1000000),



                "diskTotal",
                Math.round(diskTotal / 1000000),



                "diskPercent",
                Math.round(diskPercent),



                "uptimeSeconds",
                Math.round(uptime)

        );

    }







    private double getMetric(String name) {


        try {


            Gauge gauge =
                    registry
                            .find(name)
                            .gauge();



            if(gauge != null) {

                return gauge.value();

            }


        } catch(Exception ignored) {

        }



        return 0;

    }







    private double getHeapMetric(String name) {


        try {


            Gauge gauge =
                    registry
                            .find(name)
                            .tag(
                                    "area",
                                    "heap"
                            )
                            .gauge();




            if(gauge != null) {

                return gauge.value();

            }




        } catch(Exception ignored) {

        }



        return 0;

    }

}

