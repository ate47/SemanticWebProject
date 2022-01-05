<template>
  <div>
    <div v-if="lastMessage.length !== 0">{{ lastMessage }}</div>
    <div @click="callTerritoire" class="cursor-pointer">
      Test Territoire scraping
    </div>
    <div @click="callDataTerritoire" class="cursor-pointer">
      Test Data Territoire scraping
    </div>
    <div @click="callMeteoCiel" class="cursor-pointer">
      Test MeteoCiel scraping
    </div>
  </div>
</template>

<script>
import api from "../api";

export default {
  name: "app",
  data: function () {
    return {
      lastMessage: "",
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
.cursor-pointer {
  cursor: pointer;
}
</style>
