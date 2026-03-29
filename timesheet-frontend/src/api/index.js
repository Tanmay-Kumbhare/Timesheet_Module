import axios from "axios"

const API = axios.create({
  baseURL: "http://localhost:8080/api"
})

export const masterApi = {
  list(resource){
    return API.get(`/${resource}`)
  },

  create(resource,data){
    return API.post(`/${resource}`,data)
  },

  update(resource,id,data){
    return API.put(`/${resource}/${id}`,data)
  },

  delete(resource,id){
    return API.delete(`/${resource}/${id}`)
  }
}

export const reportApi = {

  async departmentwise(params){
    const res = await API.get("/reports/departmentwise",{params})
    return res.data
  },

  async institutewise(params){
    const res = await API.get("/reports/institutewise",{params})
    return res.data
  },

  async notificationLogs(params){
    const res = await API.get("/reports/notificationLogs",{params})
    return res.data
  }

}

export const refApi = {

  async departments(){
    const res = await API.get("/departments")
    return res.data
  },

  async employees(departmentId){
    const res = await API.get("/employees",{params:{departmentId}})
    return res.data
  }

}