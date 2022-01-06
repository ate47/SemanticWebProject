<template>
  <div>
    <div v-show="floorLoaded">
      <div :key="floor" v-for="floor in floorData">
        <floor :floor="floor" />
      </div>
    </div>
  </div>
</template>

<script>
import api from "../api";
import Floor from "./Floor.vue";
import { ref } from "vue";

export default {
  components: { Floor },
  name: "floors",
  setup() {
    let floorData = ref([]);
    let floorLoaded = ref(false);
    api
      .floors()
      .then((array) => {
        floorData.value = array;
        floorLoaded.value = true;
      })
      .catch(console.error);

    return {
      floorLoaded,
      floorData,
    };
  },
  methods: {
    callTerritoire: async function () {
      const answer = await api.territoire();
      this.lastMessage = `used the scraper '${answer.usedScraper}' to add ${answer.newTriple} tiple(s)`;
    },
    callDataTerritoire: async function () {
      const answer = await api.dataterritoire();
      this.lastMessage = `used the scraper '${answer.usedScraper}' to add ${answer.newTriple} tiple(s)`;
    },
    callMeteoCiel: async function () {
      const answer = await api.meteociel();
      this.lastMessage = `used the scraper '${answer.usedScraper}' to add ${answer.newTriple} tiple(s)`;
    },
  },
};
</script>

<style>
</style>