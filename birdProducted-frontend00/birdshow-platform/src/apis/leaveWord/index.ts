import http from "@/utils/http.ts";

// 查询反馈区域列表
export function getLeaveWordList(id?: any) {
    return http({
        url: '/leaveWord/list',
        method: 'get',
        params: {
            id
        }
    })
}

// 新增反馈区域
export function userLeaveWord(content: any) {
    return http.post('/leaveWord/auth/userLeaveWord',JSON.stringify(content))
}