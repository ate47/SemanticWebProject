import { createApp } from "vue";
import App from "./components/App.vue";
import Vuelidate from "vuelidate";

createApp(App).mount("#app");
const options = {
  isEnabled: true,
  logLevel: "debug",
  stringifyArguments: false,
  showLogLevel: true,
  showMethodName: false,
  separator: "|",
  showConsoleColors: true,
};
App.use(Vuelidate, options);
