import Vue from 'vue'
import Router from 'vue-router'
// 路由模块导入（按功能分类）

Vue.use(Router)

/* Layout：若依主布局组件（含顶部导航+侧边栏） */
import Layout from '@/layout'

/**
 * Note: 路由配置项说明
 * hidden: true          // 不在侧边栏显示（如登录页、404页）
 * alwaysShow: true      // 强制显示根路由（即使子路由只有1个）
 * redirect: noRedirect  // 面包屑不可点击
 * name: 'router-name'   // 路由唯一标识（keep-alive必用）
 * meta: {
 *   title: 'title'      // 侧边栏/面包屑显示名称
 *   icon: 'svg-name'    // 侧边栏图标
 *   roles: ['admin']    // 访问角色权限
 *   affix: true         // 固定在标签栏
 * }
 */

// 公共路由（所有用户可访问）
export const constantRoutes = [
  // 路由重定向页面（hidden: true）
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  // 登录页（hidden: true）
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  // 注册页（hidden: true）
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  // 404错误页（hidden: true）
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  // 401错误页（hidden: true）
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },

  // 核心功能路由（按显示优先级排序）
  // 1. 默认首页（控制台，固定在标签栏）
  {
    path: '',
    component: Layout,
    redirect: 'index', // 根路径默认跳转控制台
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '系统首页', icon: 'dashboard', affix: true }  // 曾用名：控制台
      }
    ]
  },
  // // 2. 自定义首页模块（侧边栏显示）
  // ...homePageRouter,
  // // 3. AI助手（侧边栏显示）
  // {
  //   path: '/ai-assistant',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'AiAssistant',
  //       component: () => import('@/views/dashboard/aiAssistant/index.vue'),
  //       meta: { title: 'AI助手', icon: 'el-icon-s-help' }
  //     }
  //   ]
  // },
  //
  // // 隐藏路由（不在侧边栏显示，仅通过跳转访问）
  // // 4. 学情分析（hidden: true）
  // ...studyInfoRouter,
  // // 5. 文件上传（hidden: true）
  // ...uploadRouter,

  // 个人中心（hidden: true）
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 动态路由（基于用户权限加载）
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  }
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push
let routerReplace = Router.prototype.replace
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
