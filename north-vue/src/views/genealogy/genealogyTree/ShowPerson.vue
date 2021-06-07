<style lang="less">
.title-font {
  font-size: 16px;
}

.number-font {
  font-size: 22px;
}

.text-right {
  text-align: right;
}

.input-length{
  width: 250px;
}

</style>
<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible" :width="600">
    <a-form v-bind="layout" :model="data">
      <a-collapse v-model:activeKey="activeKey">
        <a-collapse-panel key="1" header="个人信息">
          <a-form-item label="名字：" name="name">
            <template v-if="edit">
              <a-input v-model:value="data.personName" class="input-length"/>
            </template>
            <template v-else>
              {{ data.personName }}
            </template>
          </a-form-item>

          <a-form-item label="性别：" name="sex">
            <template v-if="edit">
              <a-select v-model:value="data.sex" style="width:250px">
                <template v-for="select in sexSelect">
                  <a-select-option :value="select.value">
                    {{ select.lable }}
                  </a-select-option>
                </template>
              </a-select>
            </template>
            <template v-else>
              {{ sexStr }}
            </template>
          </a-form-item>

          <a-form-item label="生日：" name="birthday">
            <template v-if="edit">
              <a-date-picker v-model:value="data.birthday" @change="onDateTimeOk($event,'birthday')" class="input-length"/>
            </template>
            <template v-else>
              {{ getDateStr(data.birthday) }}
            </template>
          </a-form-item>
          <a-form-item label="逝日：" name="deathday">
            <template v-if="edit">
              <a-date-picker v-model:value="data.deathday" @change="onDateTimeOk($event,'deathday')" class="input-length"/>
            </template>
            <template v-else>
              {{ getDateStr(data.deathday) }}
            </template>
          </a-form-item>
          <a-form-item label="是谁子女：" name="parentId" v-if="!!data.parentId?(true):edit"  >
            <template v-if="edit" >
              <a-select v-model:value="data.parentId" style="width:250px" show-search @search="debounceSearch($event,'parentNameOption')" :filterOption="false" allowClear placeholder="首字查询，只有一个生效">
                <template v-for="select in parentNameOption">
                  <a-select-option :value="select.value">
                    {{ select.lable }}
                  </a-select-option>
                </template>
              </a-select>
            </template>
            <template v-else>
              {{ parentName }}
            </template>
          </a-form-item>
          <a-form-item label="是谁配偶：" name="partnerId"  v-if="!!data.partnerId?(true):edit"  >
            <template v-if="edit" >
              <a-select v-model:value="data.partnerId" style="width:250px" show-search @search="debounceSearch($event,'partnerNameOption')" :filterOption="false" allowClear placeholder="首字查询，只有一个生效">
                <template v-for="select in partnerNameOption">
                  <a-select-option :value="select.value">
                    {{ select.lable }}
                  </a-select-option>
                </template>
              </a-select>
            </template>
            <template v-else>
              {{ partnerName }}
            </template>
          </a-form-item>

        </a-collapse-panel>
        <a-collapse-panel key="2" header="相片" v-if="!!fileList.length>0?(true):edit">
          <a-form-item :wrapperCol="{span: 24}" >
            <a-upload :before-upload="beforeUpload" :customRequest="customRequest" :file-list="fileList"
                      list-type="picture-card"
                      @change="handleChange" @preview="handlePreview"
            >
              <div v-if="!!edit && fileList.length < 5">
                <plus-outlined/>
                <div class="ant-upload-text">上传</div>
              </div>
            </a-upload>

          </a-form-item>
        </a-collapse-panel>

        <a-collapse-panel key="3" header="时间轴" v-if="!!timelines  .length>0?(true):edit">
          <template v-if="edit">
            <a-row v-for=" (timeline,index) in timelines" :key="'timelines'+index">
              <a-col :span="10" >
                <a-form-item label="时间：" name="name">
                  <a-date-picker v-model:value="timeline.eventTime" />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="经历：" name="name">
                  <a-textarea v-model:value="timeline.story" class="input-length"/>
                </a-form-item>
              </a-col>
              <a-col :span="2">
                <a-button type="primary" @click="removeTimeLine(index)">
                  <template #icon><MinusOutlined /></template>
                </a-button>
              </a-col>
            </a-row>

            <a-row type="flex" justify="center" align="middle">
              <a-button type="primary" @click="addTimeLine">
                <template #icon><PlusOutlined /></template>
              </a-button>
            </a-row>
          </template>
          <template v-else>
            <template v-if="timelines.length>0">
              <a-timeline >
                <a-timeline-item v-for=" (timeline) in timelines" :key="'timelines'+index">
                  <a-row>
                    <a-col>
                      时间：
                    </a-col>
                    <a-col>
                      {{ getDateStr(timeline.eventTime) }}
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col>
                      经历：
                    </a-col>
                    <a-col>
                      {{ timeline.story }}
                    </a-col>
                  </a-row>
                </a-timeline-item>
              </a-timeline>
            </template>
            <template v-else>
              没有时间轴
            </template>
          </template>

        </a-collapse-panel>

        <template v-if="edit && !!data.id">
          <a-collapse-panel key="4" header="添加">
            <a-space>
              <a-button type="primary" @click="open({parentId:data.id},true)">子女</a-button>
              <a-button type="primary" @click="open({parterId:data.id},true)">配偶</a-button>
              <a-button type="primary" @click="remove">删除此人</a-button>
            </a-space>

          </a-collapse-panel>
        </template>
      </a-collapse>
    </a-form>

    <template #footer>
      <a-button key="edit" @click="toEdit" v-if="!!data.id">
        {{edit?"取消编辑":"编辑"}}
      </a-button>
      <a-button key="cancel" @click="cancel">取消</a-button>
      <a-button key="ok" type="primary" :loading="loading" @click="ok">确定</a-button>
    </template>
    <a-modal :footer="null" :visible="previewVisible" centered @cancel="previewCancel">
      <img :src="previewImage" style="width: 100%"/>
    </a-modal>
  </a-modal>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import SysDictApi from "@/api/SysDictApi";
import {AxiosResponse} from "axios";
import {Moment} from "moment";
import GenealogyApi from '@/api/GenealogyApi';
import {createVNode} from "vue";


@Options({
  name: "showPerson",
  data() {
    return {
      visible: false,
      previewVisible:false,
      previewImage:"",
      loading:false,
      title: "添加",
      activeKey: 1,
      edit: true,
      initData:{
        sex: 1
      },
      data: {},
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      sexSelect: [],
      fileList: [],
      timelines:[],
      parentName:"",
      parentNameOption:[],
      partnerName:"",
      partnerNameOption:[],
    }
  },
  props: {
    okCallback: {
      type: Function,
      default: (data: any) => {

      }
    },
  },
  methods: {
    toEdit(){
      this.edit = !this.edit
    },
    open(data:any,isEdit:Boolean) {
      this.data = this.initData
      this.loading = false
      this.fileList = []
      this.timelines = []
      this.activeKey = 1
      if(!!data){
        this.data = data
        this.getParentName()
        this.getPartnerName()
        GenealogyApi.getGenealogyPersonImages(data.id).then(res=>{
          this.fileList = []
          for (let idKey in res.data.data) {
            this.fileList.push({url: window.BASE_URL+"/sysFile/download?id="+res.data.data[idKey]})
          }
        })
        GenealogyApi.getGenealogyPersonTimeLines(data.id).then(res=>{
          this.timelines = res.data.data
        })
      }else{
        this.data = {
          sex: 1
        }
      }
      this.edit = isEdit
      this.visible = true
    },
    async ok() {
      this.loading = true
      const formData = new FormData();
      formData.append("genealogyId", "1")
      for (let dataKey in this.data) {
        if (this.data[dataKey] != null) {
          formData.append(dataKey, this.data[dataKey])
        }
      }
      if (this.fileList.length > 0) {
        this.fileList.forEach((file: any) => {
          if(!!file.url){
            let fileId = file.url.replace(window.BASE_URL+"/sysFile/download?id=","")
            formData.append("fileId", fileId );
            console.log(fileId)
            return
          }
          console.log(file)
          formData.append("file", file.originFileObj as any);
        });
      }
      if (this.timelines.length > 0) {
        formData.append("personTimelines", JSON.stringify(this.timelines));
      }
      if(!!this.data.id){
        await GenealogyApi.editGenealogyPerson(formData).then(res =>{
          this.loading = false
          this.visible = false

        })
      }else{
        await GenealogyApi.addGenealogyPerson(formData).then(res =>{
          this.loading = false
          this.visible = false

        })
      }
      this.okCallback()

    },
    cancel() {
      this.visible = false
    },
    onDateTimeOk(date: Moment, item: string) {
      this.data[item] = date.toDate().Format('yyyy-MM-dd')
    },
    beforeUpload(file: any) {
      if (!file) {
        return false
      }
      console.log(file.type)
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/bmp';
      if (!isJpgOrPng) {
        this.$message.error("上传文件格式不支持，请上传jpg、png、bmp格式");
        return false
      }
      const mb = 5
      const isLtSize = file.size / 1024 / 1024 < mb;
      if (!isLtSize) {
        this.$message.error("文件大小超出限制，最大"+ mb +"M");
        return false
      }

      return true
    },
    handleChange(fileItem: any) {
      if(!this.edit){
        return
      }
      let file = fileItem.file
      if (file.status == 'uploading') {
        file.status = 'done'
        this.fileList = [...this.fileList, file];
      }
      if (file.status == 'removed') {
        const index = this.fileList.indexOf(file);
        this.fileList.splice(index, 1);
      }
    },
    customRequest(file: any) {
      //不做任何实现不会自动上传
    },
    async transformFile(file: any) {
      return new Promise(resolve => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          const canvas = document.createElement('canvas');
          const image: HTMLImageElement = document.createElement('img');
          image.src = reader.result as string;
          image.onload = () => {
            const ctx: CanvasRenderingContext2D = canvas.getContext('2d')!;
            if (image.width > 0 && image.height > 0) {
              if (image.width / image.height >= 164 / 112) {
                if (image.width > 164) {
                  image.width = 164;
                  image.height = (image.height * 164) / image.width;
                } else {
                  image.width = image.width;
                  image.height = image.height;
                }
                image.alt = image.width + "×" + image.height;
              } else {
                if (image.height > 112) {
                  image.height = 112;
                  image.width = (image.width * 112) / image.height;
                } else {
                  image.width = image.width;
                  image.height = image.height;
                }
                image.alt = image.width + "×" + image.height;
              }
            }
            ctx.drawImage(image, 0, 0);
            canvas.toBlob(resolve);
          };
        };
      });
    },
    async handlePreview(file: any) {
      if (!!file.url) {
        this.previewImage = file.url
      } else {
        this.previewImage = await this.loadImage(file.originFileObj)
      }
      this.previewVisible = true
    },
    previewCancel() {
      this.previewVisible = false
    },
    addTimeLine(){
      this.$nextTick(()=>{
        this.timelines.push({})
      })
    },
    removeTimeLine(index:number){
      this.timelines.splice(index, 1);
    },
    getDateStr(value:any){
      if(!!value){
        return new Date(value).Format("yyyy-MM-dd")
      }
      return ""
    },
    getParentName(){
      if(!!this.data.parentId){
        GenealogyApi.getGenealogyPerson(this.data.parentId).then(res=>{
          this.parentNameOption = [{value:res.data.data.id,lable:res.data.data.personName}]
          this.parentName = res.data.data.personName
        })
      }else{
        this.parentName = ""
      }

    },
    getPartnerName(){
      if(!!this.data.partnerId){
        GenealogyApi.getGenealogyPerson(this.data.partnerId).then(res=>{
          this.partnerNameOption = [{value:res.data.data.id,lable:res.data.data.personName}]
          this.partnerName = res.data.data.personName
        })
      }else{
        this.partnerName = ""
      }
    },
    async search(value:string){
      let option: { value: any; lable: any; }[] = []
      await GenealogyApi.getGenealogyTreePersonSearch("1",value).then(res=>{
          for (let index in res.data.data) {
            option.push({value:res.data.data[index].id,lable:res.data.data[index].personName})
          }
      })
      return option
    },
    remove(){
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          GenealogyApi.removeGenealogyPerson(this.data.id).then(res =>{
            console.log(this.data)
            this.okCallback()
            this.cancel()
          })
        },
      });

    },
  },
  computed: {
    sexStr() {
      let sexStr = "未知";
      switch (this.data.sex) {
        case 1:
          sexStr = "男"
          break
        case 2:
          sexStr = "女"
          break
        default:
          sexStr = "未知"
          break
      }
      return sexStr
    },
    birthdayStr(){
      if(!!this.data.birthday){
        return new Date(this.data.birthday).Format("yyyy-MM-dd")
      }
      return ""
    },
    deathdayStr(){
      if(!!this.data.deathday){
        return new Date(this.data.deathday).Format("yyyy-MM-dd")
      }
      return ""
    },
  },
  created() {
    this.debounceSearch = this.$lodash.debounce((value:string,option:string)=>{
      this.search(value).then((data:Array<any>) =>{
        this[option] = data
      })
    },600)
    SysDictApi.getSelect('性别').then((res: AxiosResponse) => {
      this.sexSelect = res.data.data
    });
  }
})

export default class ShowPerson extends Vue {
}
</script>

