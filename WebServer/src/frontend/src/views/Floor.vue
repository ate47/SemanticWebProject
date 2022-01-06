<template>
  <div>
    <router-link to="/">...Floors</router-link>
    <div v-if="floorLoaded">
      <div class="text-2xl text-black">{{ floorData.label }}</div>
      <ul>
        <li :key="room" v-for="room in floorData.rooms">
          <router-link
            :to="'/room/' + encodeURIComponent(room.iri)"
            class="text-gray-700"
          >
            {{ room.label }}
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
  name: "Floor",
  setup() {
    const route = useRoute();
    let floorData = ref({});
    let floorLoaded = ref(false);
    api
      .floor(encodeURIComponent(route.params.id))
      .then((floor) => {
        floorData.value = floor;
        floorLoaded.value = true;
      })
      .catch(console.error);

    return {
      floorLoaded,
      floorData,
    };
  },
};
</script>
