<template>
  <div>
    <router-link to="/">...Floors</router-link>
    <div v-if="roomLoaded">
      <div class="text-2xl text-black">{{ roomData.label }}</div>
      <ul>
        <li v-if="roomData.sensors.length === 0">No sensor for this room 😢</li>
        <li :key="sensor" v-for="sensor in roomData.sensors">
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
  name: "Room",
  setup() {
    const route = useRoute();
    let roomData = ref({});
    let roomLoaded = ref(false);
    api
      .room(encodeURIComponent(route.params.id))
      .then((room) => {
        roomData.value = room;
        roomLoaded.value = true;
      })
      .catch(console.error);

    return {
      roomLoaded,
      roomData,
    };
  },
};
</script>
