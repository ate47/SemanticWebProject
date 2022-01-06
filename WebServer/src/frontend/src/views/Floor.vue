<template>
  <div>
    <div v-if="floorLoaded">
      {{ floorData.label }}
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
    console.log(this);
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
