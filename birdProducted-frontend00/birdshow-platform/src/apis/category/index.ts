import http from "@/utils/http.ts";

export function birdCateList() {
    return http.get("/birdcategories/list", {
        method: "get"
    });
}