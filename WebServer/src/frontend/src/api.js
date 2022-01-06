import axios from "axios";

const backendEndpoint = "http://localhost:8080/api";

export default {
  /**
   * request the scraping of the territoire kg
   * @returns {Promise<any>} the api return value
   */
  territoire: async function () {
    return axios
      .get(backendEndpoint + "/territoire")
      .then((result) => result.data);
  },
  /**
   * request the scraping of the territoire kg csv
   * @returns {Promise<any>} the api return value
   */
  dataterritoire: async function () {
    return axios
      .get(backendEndpoint + "/dataterritoire")
      .then((result) => result.data);
  },
  /**
   * request the scraping of the meteociel
   * @returns {Promise<any>} the api return value
   */
  meteociel: async function (day = 0, month = 0, year = 0) {
    return axios
      .get(
        backendEndpoint +
          "/meteociel?day=" +
          day +
          "&month=" +
          month +
          "&year=" +
          year
      )
      .then((result) => result.data);
  },
};