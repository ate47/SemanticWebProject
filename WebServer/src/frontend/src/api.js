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
   * requests the floors of the main building
   * @returns {Promise<Array<any>>} the api return value
   */
  floors: async function () {
    return axios
      .get(backendEndpoint + "/floors")
      .then((result) => result.data.floors);
  },

  /**
   * requests the rooms of a floor
   * @param {string} floor the floor
   * @returns {Promise<Array<any>>} the api return value
   */
  rooms: async function (floor) {
    return axios
      .get(backendEndpoint + "/rooms?floor=" + floor)
      .then((result) => result.data);
  },

  /**
   * requests a floor
   * @param {string} floor the floor
   * @returns {Promise<any>} the api return value
   */
  floor: async function (floor) {
    return axios
      .get(backendEndpoint + "/floor?floor=" + floor)
      .then((result) => result.data.floors);
  },

  /**
   * requests a room
   * @param {string} room the room
   * @returns {Promise<any>} the api return value
   */
  room: async function (room) {
    return axios
      .get(backendEndpoint + "/room?room=" + room)
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
