<template>
  <div>
    <div v-if="sensorLoaded">
      <table>
        <tr v-if="sensorData.length === 0">
          <th>No data for this sensor 😢</th>
        </tr>
        <tr v-else>
          <th>temperature</th>
          <th>year</th>
          <th>month</th>
          <th>day</th>
          <th>hours</th>
          <th>external temp</th>
        </tr>
        <tr :key="sensor" v-for="sensor in sensorData">
          <td>{{ sensor.temperature }}</td>
          <td>{{ sensor.year }}</td>
          <td>{{ sensor.month }}</td>
          <td>{{ sensor.day }}</td>
          <td>{{ sensor.hours }}</td>
          <td>{{ sensor.externaltemp }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import api from "../api";
import { ref } from "vue";
import { useRoute } from "vue-router";

export default {
  name: "Sensor",
  setup() {
    const route = useRoute();
    let sensorData = ref({});
    let sensorLoaded = ref(false);
    api
      .sensor(encodeURIComponent(route.params.id))
      .then((sensor) => {
        sensorData.value = sensor;
        sensorLoaded.value = true;
      })
      .catch(console.error);

    return {
      sensorLoaded,
      sensorData,
    };
  },
};
</script>
