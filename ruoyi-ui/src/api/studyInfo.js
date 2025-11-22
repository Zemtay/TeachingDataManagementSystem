import request from '@/utils/request'

export function getTeacherStudyinfo() {
  return request({
    url: '/dashboard/studyinfo/getTeacherStudyinfo',
    method: 'get',
  })
}

export function getExecutorStudyinfo() {
  return request({
    url: '/dashboard/studyinfo/getTeacherStudyinfo',
    method: 'get',
  })
}

export function getAiReport(msg) {
  return request({
    url: '/dashboard/studyinfo/getAiReport',
    method: 'post',
    data: { msg }
  })
}
