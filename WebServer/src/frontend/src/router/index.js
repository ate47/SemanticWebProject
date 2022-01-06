import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "Home",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Home.vue"),
  },
  {
    path: "/floor/:id",
    name: "Floor",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Floor.vue"),
  },
  {
    path: "/room/:id",
    name: "Room",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Room.vue"),
  },
  {
    path: "/sensor/:id",
    name: "Sensor",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Sensor.vue"),
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
