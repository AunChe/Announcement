import { createRouter, createWebHistory } from 'vue-router';
import ListsAnnouncement from '../views/ListsAnnouncement.vue';
import OneAnnounce from '../components/OneAnnounce.vue';
import AddAnnounce from '../components/AddAnnounce.vue';
import { getAnnouncementById } from '../composable/getAnnouncement';
import EditAnnounce from '../components/EditAnnounce.vue';
import UserViewListAnnounce from '../views/UserViewListAnnounce.vue';
import UserViewOneAnnounce from '../components/UserViewOneAnnounce.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/admin/announcement',
      name: 'ListAllAnnouncement',
      component: ListsAnnouncement,
    },
    {
      path: '/admin/announcement/:id',
      name: 'OneAnnouncement',
      component: OneAnnounce,
      props: true,
      beforeEnter: async (to, from, next) => {
        const id = Number(to.params.id);
        if (!Number.isInteger(id)) {
          alert('The request page is not available');
          next('/admin/announcement');
        } else {
          try {
            const announcement = await getAnnouncementById(id);
            if (!announcement) {
              alert('The request page is not available');
              next('/admin/announcement');
            } else {
              next();
            }
          } catch (error) {
            alert('The request page is not available');
            console.log(error);
            next('/admin/announcement');
          }
        }
      },
    },
    {
      path: '/admin/announcement/add',
      name: 'AddNewAnnouncement',
      component: AddAnnounce,
    },
    {
      path: '/admin/announcement/:id/edit',
      name: 'EditAnnouncement',
      component: EditAnnounce,
    },
    {
      path: '/announcement',
      name: 'UserViewListAnnouncement',
      component: UserViewListAnnounce
      
    },
    {
      path: '/announcement/:id',
      name: 'UserViewOneAnnouncement',
      component: UserViewOneAnnounce,
      props: true,
      beforeEnter: async (to, from, next) => {
        const id = Number(to.params.id);
        if (!Number.isInteger(id)) {
          alert('The request page is not available');
          next('/announcement');
        } else {
          try {
            const announcement = await getAnnouncementById(id);
            if (!announcement) {
              alert('The request page is not available');
              next('/announcement');
            } else {
              next();
            }
          } catch (error) {
            alert('The request page is not available');
            console.log(error);
            next('/announcement');
          }
        }
      },
    }
  ]
});
export default router;
