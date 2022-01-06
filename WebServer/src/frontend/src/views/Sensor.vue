<template>
  <div>
    <div v-if="sensorLoaded">
      <router-link :to="'/floor/' + encodeURIComponent(sensorData.floor.iri)"
        >...{{ sensorData.floor.label }}</router-link
      >
      <div class="text-2xl text-black">{{ sensorData.label }}</div>
      <ul>
        <li v-if="sensorData.sensors.length === 0">
          No data for this sensor 😢
        </li>
        <li :key="sensor" v-for="sensor in sensorData.sensors">
          <router-link
            :to="'/sensor/' + encodeURIComponent(sensor)"
            class="text-gray-700"
          >
            {{ sensor }}
          </router-link>
        </li>
      </ul>
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
