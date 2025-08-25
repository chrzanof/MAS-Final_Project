import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CoursesView from '../views/CoursesView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import authService from '../services/authService.js'
import MyCoursersView from '../views/MyCoursersView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/courses',
      name: 'courses',
      component: CoursesView,
      meta: { requiresAuth: true }
    },
    {
      path: '/my-courses',
      name: 'my-courses',
      component: MyCoursersView,
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { redirectIfAuth: true }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { redirectIfAuth: true }
    }
  ]
})

// Route guards
router.beforeEach(async (to, from, next) => {
  const isAuthenticated = await authService.checkAuthStatus()

  // If route requires authentication and user is not authenticated
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/')
    return
  }

  // If user is authenticated and trying to access login/register/home, redirect to courses
  if ((to.meta.redirectIfAuth || to.path === '/') && isAuthenticated) {
    next('/courses')
    return
  }

  // If trying to access my-courses and user is not a teacher
  if (to.path === '/my-courses' && isAuthenticated) {
    const user = authService.getUser()
    if (!user || !user.teacher) {
      next('/courses') // Redirect non-teachers to browse courses
      return
    }
  }

  next()
})

export default router
