(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2751ce98"],{"02e7":function(e,t,a){"use strict";var o=a("ee7e"),s=a.n(o);s.a},"09a3":function(e,t,a){"use strict";var o=a("7fb6"),s=a.n(o);s.a},"0da4":function(e,t,a){"use strict";var o=a("136e"),s=a.n(o);s.a},"136e":function(e,t,a){},"2b9b":function(e,t,a){},"6a10":function(e,t,a){"use strict";var o=a("964c"),s=a.n(o);s.a},7257:function(e,t,a){"use strict";var o=a("d471"),s=a.n(o);s.a},"7ee0":function(e,t,a){"use strict";var o=a("b3677"),s=a.n(o);s.a},"7fb6":function(e,t,a){},"964c":function(e,t,a){},"99fa":function(e,t,a){},a8b1:function(e,t,a){"use strict";var o=a("2b9b"),s=a.n(o);s.a},b3677:function(e,t,a){},b3cb:function(e,t,a){"use strict";var o=a("99fa"),s=a.n(o);s.a},d43f:function(e,t,a){"use strict";a.r(t);var o=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"workspace"},[a("el-tabs",{attrs:{type:"border-card"},on:{"tab-click":e.tabClick}},[a("el-tab-pane",{attrs:{label:"审批列表"}},[a("el-row",{staticStyle:{"margin-bottom":"20px"}},[a("el-col",{attrs:{xs:12,sm:10,md:8,lg:6,xl:4}},[a("el-input",{attrs:{size:"medium",placeholder:"搜索表单",clearable:""},model:{value:e.formList.inputs,callback:function(t){e.$set(e.formList,"inputs",t)},expression:"formList.inputs"}},[a("i",{staticClass:"el-input__icon el-icon-search",attrs:{slot:"prefix"},slot:"prefix"})])],1)],1),a("el-collapse",{directives:[{name:"show",rawName:"v-show",value:e.formList.inputs.length<1,expression:"formList.inputs.length < 1"}],model:{value:e.actives,callback:function(t){e.actives=t},expression:"actives"}},e._l(e.formList.list,(function(t,o){return a("el-collapse-item",{directives:[{name:"show",rawName:"v-show",value:t.items.length>0&&t.id>0,expression:"group.items.length > 0 && group.id > 0"}],key:o,attrs:{title:t.name,name:t.name}},[a("div",e._l(t.items,(function(t,o){return a("div",{key:o,staticClass:"form-item",on:{click:function(a){return e.enterItem(t)}}},[a("i",{class:t.logo.icon,style:"background: "+t.logo.background}),a("div",[a("ellipsis",{attrs:{"hover-tip":"",content:t.formName}}),a("span",[e._v("发起审批")])],1)])})),0)])})),1),e.formList.inputs.length>0?a("div",[e._l(e.formList.searchResult,(function(t,o){return a("div",{key:o,staticClass:"form-item",on:{click:function(a){return e.enterItem(t)}}},[a("i",{class:t.logo.icon,style:"background: "+t.logo.background}),a("div",[a("ellipsis",{attrs:{"hover-tip":"",content:t.formName}}),a("span",[e._v("发起审批")])],1)])})),a("div",{directives:[{name:"show",rawName:"v-show",value:0===e.formList.searchResult.length&&e.formList.inputs.length>0,expression:"formList.searchResult.length === 0 && formList.inputs.length > 0"}],staticClass:"no-data"},[e._v(" 没有找到相关的表单 😥 ")])],2):e._e()],1),a("el-tab-pane",{attrs:{name:"pending",label:"待我处理("+(e.$refs.pending?e.$refs.pending.total:0)+")"}},[a("pending",{ref:"pending"})],1),a("el-tab-pane",{attrs:{name:"started",label:"我发起的"}},[a("started",{ref:"started"})],1),a("el-tab-pane",{attrs:{name:"end",label:"我处理的"}},[a("end",{ref:"end"})],1),a("el-tab-pane",{attrs:{label:"抄送给我的"}},[a("cc",{ref:"cc"})],1)],1),a("start",{ref:"startDialog",attrs:{formId:e.selectForm.formId},on:{submitProcess:e.submitForm}})],1)},s=[],r=(a("4160"),a("c975"),a("b0c0"),a("159b"),a("b85c")),n=a("4e02"),i=a("0c6d");function c(e){return Object(i["a"])({url:"process-instance/startProcessInstance",method:"post",data:e})}function l(e){return Object(i["a"])({url:"process-instance/queryMineTask",method:"post",data:e})}function d(e){return Object(i["a"])({url:"process-instance/queryMineEndTask",method:"post",data:e})}function u(e){return Object(i["a"])({url:"process-instance/queryMineStarted",method:"post",data:e})}function h(e){return Object(i["a"])({url:"process-instance/queryMineCC",method:"post",data:e})}function f(e){return Object(i["a"])({url:"task/queryTask",method:"get",params:e})}function p(e){return Object(i["a"])({url:"task/back",method:"post",data:e})}function m(e){return Object(i["a"])({url:"process-instance/showImg?procInsId="+e,method:"get"})}function g(e){return Object(i["a"])({url:"task/completeTask",method:"post",data:e})}function v(e){return Object(i["a"])({url:"task/delegateTask",method:"post",data:e})}function b(e){return Object(i["a"])({url:"task/resolveTask",method:"post",data:e})}function w(e){return Object(i["a"])({url:"task/stopProcessInstance",method:"post",data:e})}function k(e){return Object(i["a"])({url:"task/setAssignee",method:"post",data:e})}function I(e){return Object(i["a"])({url:"processCopy/querySingleDetail",method:"get",params:e})}function y(e){return Object(i["a"])({url:"process-instance/formatStartNodeShow",method:"post",data:e})}function D(e){return Object(i["a"])({url:"process-instance/detail",method:"get",params:e})}var S=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}]},[e.loading?e._e():a("div",[a("form-render",{ref:"form",staticClass:"process-form",attrs:{forms:e.forms},model:{value:e.formData,callback:function(t){e.formData=t},expression:"formData"}})],1),a("el-divider",[e._v("审批流程")]),a("div",[a("FlowProcessNodeShow",{key:e.k,ref:"flowProcessNodeRef",attrs:{formData:e.formData,processInstanceId:"",row:e.form.nodeShow},on:{childEvent:e.selectNodeUser}})],1)],1)},P=[],_=a("2b36"),x=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-timeline",{attrs:{reverse:!1}},e._l(e.row,(function(t,o){return a("el-timeline-item",{key:o,attrs:{size:"large",color:2!=t.status?1==t.status?"green":"grey":"blue",icon:e.nodeIconShow(t.type)}},[a("p",[e._v(e._s(t.name)+" "),t.placeholder&&t.placeholder.length>0?[e._v("["+e._s(t.placeholder)+"]")]:e._e()],2),t.userDtoList&&t.userDtoList.length>0?a("div",e._l(t.userDtoList,(function(t,o){return a("el-card",{key:o,staticClass:"box-card",staticStyle:{"margin-bottom":"10px"}},e._l(t,(function(t,o){return a("div",{key:o,staticClass:"node-show"},[a("div",{staticStyle:{overflow:"hidden"}},[a("div",{staticClass:"d1"},[a("div",[a("el-avatar",{attrs:{shape:"square",size:30,src:t.avatar}}),2===t.status?a("i",{staticClass:"el-icon-finished avatar-icon",staticStyle:{color:"white","background-color":"red"}}):e._e(),1===t.status?a("i",{staticClass:"el-icon-time avatar-icon",staticStyle:{color:"white","background-color":"green"}}):e._e()],1),a("div",{staticStyle:{"font-size":"8px",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis","text-align":"left"}},[e._v(" "+e._s(t.name)+" ")])]),a("div",{staticClass:"d2"},[a("div",[e._v(e._s(t.showTime))]),a("div",["COMPLETE"===t.operType?a("el-tag",{attrs:{type:"success"}},[e._v("完成")]):e._e(),"DELEGATION"===t.operType?a("el-tag",{attrs:{type:"danger"}},[e._v("委派")]):e._e(),"RESOLVED"===t.operType?a("el-tag",{attrs:{type:"warning"}},[e._v("解决")]):e._e()],1)])]),t.approveDesc&&t.approveDesc.length>0?a("p",{staticStyle:{"text-align":"left","background-color":"lightgrey",padding:"10px 10px"}},[e._v(e._s(t.approveDesc))]):e._e()])})),0)})),1):e._e(),t.selectUser?[a("div",{staticStyle:{"flex-direction":"row",display:"flex"}},[e._l(e.nodeUser[t.id],(function(t,o){return a("div",{key:o,staticStyle:{width:"50px",height:"50px"}},[a("el-avatar",{attrs:{shape:"square",size:30,src:t.avatar}}),a("div",{staticStyle:{"text-align":"left","font-size":"8px",width:"50px",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis"}},[e._v(" "+e._s(t.name)+" ")])],1)})),e.processInstanceId&&0!=e.processInstanceId.length?e._e():a("div",{staticStyle:{width:"50px",height:"50px"}},[a("i",{staticClass:"el-icon-circle-plus-outline self-select-user-icon",style:{color:e.nodeUser[t.id]&&e.nodeUser[t.id].length>0?"black":"red"},on:{click:function(a){return e.showSelectUser(t)}}})])],2)]:e._e(),t.branch.length>0?a("el-tabs",{attrs:{type:"border-card"}},e._l(t.branch,(function(t,o){return a("el-tab-pane",{key:o,attrs:{label:"分支"+(o+1),name:o+""}},[t.placeholder&&t.placeholder.length>0?[e._v("["+e._s(t.placeholder)+"]")]:e._e(),a("div",{staticStyle:{padding:"0px 5px"}},[a("flow-process-node-show",{ref:"flowProcessNodeRef",refInFor:!0,attrs:{formData:e.formData,processInstanceId:e.processInstanceId,row:t.children},on:{childEvent:e.selectNodeUser}})],1)],2)})),1):e._e()],2)})),1),a("org-picker",{ref:"orgPicker",attrs:{title:"选择用户",multiple:e.multipleSelectUser,type:"user",selected:e.orgPickerSelected},on:{ok:e.selected}})],1)},C=[],N={APPROVAL:{icon:"el-icon-s-check",color:"rgb(255, 148, 62)"},DELAY:{icon:"el-icon-time",color:"#f25643"},CC:{icon:"el-icon-s-promotion",color:"rgb(50, 150, 250)"},TRIGGER:{icon:"el-icon-set-up",color:"#15BC83"},INCLUSIVES:{icon:"el-icon-s-grid",color:"#718dff"},CONCURRENTS:{icon:"el-icon-s-operation",color:"#718dff"},CONDITIONS:{icon:"el-icon-share",color:"rgb(21, 188, 131)"},CHILD_PROCESS:{icon:"el-icon-guide",color:"#1e90ff"}},$={nodeParam:N},O=a("709c"),L={name:"flow-process-node-show",components:{OrgPicker:O["a"]},props:{row:{type:Array,default:function(){return[]}},formData:{type:Object,default:function(){}},processInstanceId:{type:String,default:""}},computed:{nodeUser:function(){var e=this.formData;if(!e)return{};var t=this.$deepCopy(e);console.log(t);var a={};for(var o in t){var s=o.indexOf("_assignee_select");s>=0?a[o.substring(0,s)]=t[o]:a[o]=t[o]}return console.log(a),a}},mounted:function(){},data:function(){return{currentNodeId:void 0,multipleSelectUser:!1,orgPickerSelected:[]}},methods:{showSelectUser:function(e){this.currentNodeId=e.id,this.multipleSelectUser=!!e.multiple&&e.multiple;var t=this.nodeUser[e.id];if(this.orgPickerSelected.length=0,t){var a,o=Object(r["a"])(t);try{for(o.s();!(a=o.n()).done;){var s=a.value;this.orgPickerSelected.push(s)}}catch(n){o.e(n)}finally{o.f()}}console.log("要显示的用户",this.orgPickerSelected),this.$refs.orgPicker.show()},selected:function(e){var t=this;this.orgPickerSelected.length=0,e.forEach((function(e){return t.orgPickerSelected.push(e)})),this.nodeUser[this.currentNodeId]=this.orgPickerSelected,this.$emit("childEvent",this.nodeUser,this.currentNodeId)},selectNodeUser:function(e,t){this.$emit("childEvent",e,t)},nodeIconShow:function(e){var t=$.nodeParam[e];return void 0==t?"":t.icon}}},T=L,j=(a("09a3"),a("2877")),U=Object(j["a"])(T,x,C,!1,null,"285d8795",null),E=U.exports,z={name:"InitiateProcess",components:{FormRender:_["a"],FlowProcessNodeShow:E},props:{code:{type:String,required:!0},defaultFormData:{type:Object,default:function(){return{}}}},data:function(){return{formDataChangeTime:(new Date).getTime(),loading:!1,formData:{},form:{formId:"",formName:"",logo:{},formItems:[],process:{},remark:"",nodeShow:[]},nodeDataUser:{},k:1}},watch:{formData:{deep:!0,handler:function(e){var t=this;this.formDataChangeTime=(new Date).getTime(),setTimeout((function(){(new Date).getTime()-t.formDataChangeTime>500&&(this.formDataChangeTime=(new Date).getTime(),t.getNodeShow())}),600)}}},mounted:function(){var e=this;this.loadFormInfo(this.code,(function(){if(e.defaultFormData)for(var t in e.defaultFormData)e.$set(e.formData,t,e.defaultFormData[t])}))},computed:{forms:function(){var e=this.$store.state.design.formItems;return e}},methods:{getNodeShow:function(){var e=this;y({formId:e.code,paramMap:e.formData}).then((function(t){e.form.nodeShow=t.data,e.k=e.k+1}))},selectNodeUser:function(e,t){this.$set(this.formData,t+"_assignee_select",e[t]),this.$emit("childEvent",e,t)},loadFormInfo:function(e,t){var a=this;this.loading=!0,Object(n["b"])(e).then((function(e){a.loading=!1;var o=e.data;o.logo=JSON.parse(o.logo),o.settings=JSON.parse(o.settings),o.formItems=JSON.parse(o.formItems),o.process=JSON.parse(o.process),a.form=o,a.$store.state.design=o,a.$emit("childEvent1");var s,n=o.process.props.formPerms,i=Object(r["a"])(n);try{for(i.s();!(s=i.n()).done;){var c=s.value,l=c.id,d=c.perm;if(a.forms){var u,h=Object(r["a"])(a.forms);try{for(h.s();!(u=h.n()).done;){var f=u.value;if(f.id===l){f.props.perm=d;break}}}catch(w){h.e(w)}finally{h.f()}}}}catch(w){i.e(w)}finally{i.f()}var p,m=o.formItems,g=Object(r["a"])(m);try{for(g.s();!(p=g.n()).done;){var v=p.value,b=v.props.value;a.$isNotBlank(b)&&a.$set(a.formData,v.id,b)}}catch(w){g.e(w)}finally{g.f()}t&&t()})).catch((function(e){a.loading=!1,a.$message.error(e)}))},validate:function(e){this.$refs.form.validate(e)}}},F=z,R=(a("02e7"),Object(j["a"])(F,S,P,!1,null,"b3357088",null)),q=R.exports,A=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.list,border:""}},[a("el-table-column",{attrs:{prop:"groupName",label:"分组",width:"180"}}),a("el-table-column",{attrs:{prop:"processName",label:"流程",width:"180"}}),a("el-table-column",{attrs:{prop:"rootUserName",label:"发起人",width:"180"}}),a("el-table-column",{attrs:{prop:"startTime",label:"流程发起时间",width:"180"}}),a("el-table-column",{attrs:{prop:"taskName",label:"当前节点",width:"180"}}),a("el-table-column",{attrs:{prop:"taskCreateTime",label:"任务时间",width:"180"}}),a("el-table-column",{attrs:{fixed:"right",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.handleClick(t.row)}}},[e._v("查看")]),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.showPic(t.row)}}},[e._v("流程图")])]}}])})],1),a("div",{staticStyle:{"text-align":"center",padding:"20px"}},[a("el-pagination",{attrs:{background:"",layout:"total, sizes, prev, pager, next, jumper","page-sizes":[10,20,50],"page-size":e.pageData.count,"current-page":e.pageData.currentPage,total:e.total},on:{"size-change":e.handleSizeChangePending,"current-change":e.handleCurrentChangePending}})],1),a("form-handler",{ref:"formHandler",on:{back:e.initList}}),a("showProcessInstanceImg",{ref:"showProcessInstanceImg"})],1)},H=[],B=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{width:"80%",title:"流程图片",visible:e.showDialogControl},on:{"update:visible":function(t){e.showDialogControl=t}}},[a("img",{staticStyle:{width:"100%"},attrs:{src:e.imgBase64}})])],1)},M=[],J={name:"workSpace",components:{},data:function(){return{showDialogControl:!1,imgBase64:""}},props:{},mounted:function(){},computed:{},methods:{showDialog:function(e){var t=this;this.showDialogControl=!0,m(e).then((function(e){t.imgBase64="data:image/png;base64,"+e.data}))}}},V=J,G=Object(j["a"])(V,B,M,!1,null,"498548f9",null),Y=G.exports,K=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-drawer",{attrs:{"destroy-on-close":"",title:e.drawerTitle,visible:e.showRight,direction:"rtl"},on:{"update:visible":function(t){e.showRight=t}}},[a("div",{staticClass:"right-drawer-1",staticStyle:{padding:"5px","background-color":"lightgrey"}},[a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"10px"}},[a("form-render",{ref:"form",staticClass:"process-form",attrs:{forms:e.formItems},model:{value:e.formData,callback:function(t){e.formData=t},expression:"formData"}})],1),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"15px"}},[a("FlowProcessNodeShow",{attrs:{processInstanceId:e.processInstanceId,row:e.nodeShow}})],1)],1),this.showOperation?a("div",{staticClass:"right-drawer-2"},[a("el-button",{attrs:{type:"danger"},on:{click:function(t){return e.showApproveDialog(!1)}}},[e._v("驳回 ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.showApproveDialog(!0)}}},[e._v("提交")])],1):e._e()]),a("add-before",{ref:"addBeforeHandler",on:{ok:e.done}}),a("add-after",{ref:"addAfterHandler",on:{ok:e.done}}),a("agree",{ref:"agreeHandler",on:{ok:e.done}}),a("reject",{ref:"rejectHandler",on:{ok:e.done}})],1)},Q=[],W=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{"destroy-on-close":"",title:"提交",visible:e.visible,width:"500px"},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",[a("el-form-item",{attrs:{label:"","label-width":"200"}},[a("el-input",{attrs:{maxlength:"200",rows:"5","show-word-limit":"",type:"textarea",placeholder:"提交意见",autocomplete:"off"},model:{value:e.desc,callback:function(t){e.desc=t},expression:"desc"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("确 定")])],1)],1)],1)},X=[],Z={name:"workSpace",components:{},props:{},data:function(){return{visible:!1,desc:"",delegationTask:!1,formData:{},taskId:""}},mounted:function(){},watch:{},computed:{},methods:{show:function(e,t,a){this.taskId=e,this.delegationTask=t,this.formData=a,this.visible=!0},submit:function(){var e=this.formData,t=this.desc,a={paramMap:e,taskId:this.taskId,taskLocalParamMap:{approveDesc:t}};this.complete(a)},complete:function(e){var t,a=this;t=this.delegationTask?b(e):g(e),t.then((function(e){a.visible=!1,a.desc="",a.$emit("ok")})).catch((function(e){a.$message.error(e.response.data)}))}}},ee=Z,te=Object(j["a"])(ee,W,X,!1,null,"1b527ae5",null),ae=te.exports,oe=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{"destroy-on-close":"",title:"提交",visible:e.visible,width:"500px"},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",[a("el-form-item",{attrs:{label:"驳回节点","label-width":"200"}},[a("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"驳回节点"},model:{value:e.targetNodeId,callback:function(t){e.targetNodeId=t},expression:"targetNodeId"}},e._l(e.rejectNodeList,(function(e,t){return a("el-option",{key:t,attrs:{label:e.name,value:e.id}})})),1)],1),a("el-form-item",{attrs:{label:"","label-width":"200"}},[a("el-input",{attrs:{maxlength:"200",rows:"5","show-word-limit":"",type:"textarea",placeholder:"提交意见",autocomplete:"off"},model:{value:e.desc,callback:function(t){e.desc=t},expression:"desc"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("确 定")])],1)],1)],1)},se=[],re={name:"workSpace",components:{},props:{},data:function(){return{visible:!1,desc:"",formData:{},taskId:"",nodeId:"",targetNodeId:"",process:{}}},mounted:function(){},watch:{},computed:{rejectNodeList:function(){var e=[],t=["APPROVAL","ROOT"],a={},o={};this.produceSerialNodeList(void 0,this.process,a,o);var s=a[this.nodeId];if(void 0==s)return[];var n,i=Object(r["a"])(s);try{for(i.s();!(n=i.n()).done;){var c=n.value,l=o[c].type;"CONCURRENTS"!==l&&"INCLUSIVES"!==l||(e=[]),t.indexOf(l)>-1&&o[c].id!=this.nodeId&&e.push({id:o[c].id,name:o[c].name})}}catch(d){i.e(d)}finally{i.f()}return e}},methods:{produceSerialNodeList:function(e,t,a,o){if(this.$isNode(t)){var s=t.id;if(o[s]=t,this.$isNotBlank(e)){var n=a[e],i=this.$deepCopy(n);i.push(s),a[s]=i}else{var c=[];c.push(s),a[s]=c}var l=t.type,d=t.children;if("CONDITIONS"===l||"CONCURRENTS"===l||"INCLUSIVES"===l){var u,h=t.branchs,f=Object(r["a"])(h);try{for(f.s();!(u=f.n()).done;){var p=u.value;this.produceSerialNodeList(s,p,a,o)}}catch(m){f.e(m)}finally{f.f()}this.$isNode(d)&&this.produceSerialNodeList(s,d,a,o)}else this.$isNode(d)&&this.produceSerialNodeList(s,d,a,o)}},show:function(e,t,a,o,s){this.taskId=e,this.delegationTask=t,this.formData=a,this.process=o,this.nodeId=s,this.rejectNodeList.length<1?this.$message.error("无可驳回节点"):(this.targetNodeId=this.rejectNodeList[this.rejectNodeList.length-1].id,this.visible=!0)},submit:function(){var e=this.formData,t=this.desc,a={paramMap:e,taskId:this.taskId,targetNodeId:this.targetNodeId,taskLocalParamMap:{approveDesc:t}};this.complete(a)},complete:function(e){var t=this;p(e).then((function(e){t.visible=!1,t.desc="",t.$emit("ok")})).catch((function(e){t.$message.error(e.response.data)}))}}},ne=re,ie=Object(j["a"])(ne,oe,se,!1,null,"5a9e758e",null),ce=ie.exports,le=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{"destroy-on-close":"",title:"提交",visible:e.visible,width:"500px"},on:{"update:visible":function(t){e.visible=t}}},[a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.showSelectUserDialog}},[e._v("选择加签人")]),a("org-items",{model:{value:e.orgPickerSelected,callback:function(t){e.orgPickerSelected=t},expression:"orgPickerSelected"}}),a("el-form",[a("el-form-item",{attrs:{label:"","label-width":"200"}},[a("el-input",{attrs:{maxlength:"200",rows:"5","show-word-limit":"",type:"textarea",placeholder:"提交意见",autocomplete:"off"},model:{value:e.desc,callback:function(t){e.desc=t},expression:"desc"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("确 定")])],1)],1),a("org-picker",{ref:"orgPicker",attrs:{title:"选择用户",type:"user",selected:e.orgPickerSelected},on:{ok:e.selected}})],1)},de=[],ue=(a("a434"),a("ec61")),he={name:"workSpace",components:{OrgPicker:O["a"],OrgItems:ue["a"]},props:{},data:function(){return{visible:!1,desc:"",taskId:"",orgPickerSelected:[]}},mounted:function(){},watch:{},computed:{},methods:{selected:function(e){var t=this;this.orgPickerSelected.length=0,e.forEach((function(e){return t.orgPickerSelected.push(e)}))},showSelectUserDialog:function(){this.$refs.orgPicker.show()},show:function(e){this.taskId=e,this.visible=!0},submit:function(){var e={targetUserId:this.orgPickerSelected[0].id,taskId:this.taskId,taskLocalParamMap:{approveDesc:this.desc}};this.complete(e)},complete:function(e){var t=this;k(e).then((function(e){t.orgPickerSelected.splice(0,t.orgPickerSelected.length),t.visible=!1,t.desc="",t.$emit("ok")})).catch((function(e){t.$message.error(e.response.data)}))}}},fe=he,pe=Object(j["a"])(fe,le,de,!1,null,"643a081c",null),me=pe.exports,ge=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{"destroy-on-close":"",title:"提交",visible:e.visible,width:"500px"},on:{"update:visible":function(t){e.visible=t}}},[a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.showSelectUserDialog}},[e._v("选择加签人")]),a("org-items",{model:{value:e.orgPickerSelected,callback:function(t){e.orgPickerSelected=t},expression:"orgPickerSelected"}}),a("el-form",[a("el-form-item",{attrs:{label:"","label-width":"200"}},[a("el-input",{attrs:{maxlength:"200",rows:"5","show-word-limit":"",type:"textarea",placeholder:"提交意见",autocomplete:"off"},model:{value:e.desc,callback:function(t){e.desc=t},expression:"desc"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("确 定")])],1)],1),a("org-picker",{ref:"orgPicker",attrs:{title:"选择用户",type:"user",selected:e.orgPickerSelected},on:{ok:e.selected}})],1)},ve=[],be={name:"workSpace",components:{OrgPicker:O["a"],OrgItems:ue["a"]},props:{},data:function(){return{delegate:!1,visible:!1,desc:"",taskId:"",orgPickerSelected:[]}},mounted:function(){},watch:{},computed:{},methods:{selected:function(e){var t=this;this.orgPickerSelected.length=0,e.forEach((function(e){return t.orgPickerSelected.push(e)}))},showSelectUserDialog:function(){this.$refs.orgPicker.show()},show:function(e){this.taskId=e,this.visible=!0},submit:function(){var e={targetUserId:this.orgPickerSelected[0].id,taskId:this.taskId,taskLocalParamMap:{delegate:this.delegate,approveDesc:this.desc}};this.complete(e)},complete:function(e){var t=this;v(e).then((function(e){t.orgPickerSelected.splice(0,t.orgPickerSelected.length),t.visible=!1,t.desc="",t.$emit("ok")})).catch((function(e){t.$message.error(e.response.data)}))}}},we=be,ke=Object(j["a"])(we,ge,ve,!1,null,"ec004288",null),Ie=ke.exports,ye={name:"workSpace",components:{FlowProcessNodeShow:E,FormRender:_["a"],Agree:ae,Reject:ce,AddAfter:me,AddBefore:Ie},props:{},data:function(){return{showOperation:!0,processInstanceId:"",showRight:!1,taskId:"",nodeId:"",formItems:[],formData:{},rowData:{},nodeShow:[],drawerTitle:"",delegationTask:!1,delegateAgain:!1,process:{}}},mounted:function(){},watch:{formData:{deep:!0,handler:function(e){var t=this;this.formDataChangeTime=(new Date).getTime(),setTimeout((function(){(new Date).getTime()-t.formDataChangeTime>500&&t.getNodeShow()}),600)}}},computed:{},methods:{getNodeShow:function(){var e=this;y({taskId:this.taskId,processInstanceId:this.processInstanceId,paramMap:this.formData}).then((function(t){e.nodeShow=t.data}))},showAddBeforeSign:function(){this.$refs.addBeforeHandler.show(this.taskId)},showAddAfterSign:function(){this.$refs.addAfterHandler.show(this.taskId)},show:function(e,t){var a=this;void 0!=t&&(this.showOperation=t),a.taskId=e.taskId,a.nodeId=e.nodeId,a.currentFlowId=e.flowId,a.processInstanceId=e.processInstanceId,a.drawerTitle=e.processName,this.handleClick(e),this.getNodeShow()},showOperationBtn:function(e){return!!this.node&&(!!this.showOperation&&(!!this.node.props&&(!!this.node.props.operationBtn&&this.node.props.operationBtn.indexOf(e)>-1)))},showApproveDialog:function(e){var t=this;this.$refs.form.validate((function(a){a?(t.formData[t.node.id+"_approve_condition"]=e,t.$refs.agreeHandler.show(t.taskId,t.delegationTask,t.formData)):t.$message.warning("请完善表单")}))},done:function(){this.showRight=!1,this.$emit("back",1)},handleClick:function(e){var t=this,a=this;if(a.taskId=e.taskId,a.nodeId=e.nodeId,a.drawerTitle=e.processName,this.$isNotBlank(a.drawerTitle)||(a.drawerTitle=e.name),a.currentFlowId=e.flowId,!this.$isNotBlank(e.taskId))return D({processInstanceId:e.processInstanceId}).then((function(e){a.node=e.data.node,a.delegationTask=e.data.delegationTask,a.delegateAgain=e.data.delegateAgain,a.process=JSON.parse(e.data.process);var t,o=Object(r["a"])(e.data.formItems);try{for(o.s();!(t=o.n()).done;){var s=t.value;a.$set(a.formData,s.id,s.value)}}catch(n){o.e(n)}finally{o.f()}a.formItems=e.data.formItems,a.showRight=!0,a.processInstanceId=e.data.processInstanceId})).catch((function(e){t.$message.error(e.response.data)})),null;f({taskId:e.taskId}).then((function(e){a.node=e.data.node,a.delegationTask=e.data.delegationTask,a.delegateAgain=e.data.delegateAgain,a.process=JSON.parse(e.data.process);var t,o=Object(r["a"])(e.data.formItems);try{for(o.s();!(t=o.n()).done;){var s=t.value;a.$set(a.formData,s.id,s.value)}}catch(n){o.e(n)}finally{o.f()}a.formItems=e.data.formItems,a.showRight=!0,a.processInstanceId=e.data.processInstanceId})).catch((function(e){t.$message.error(e.response.data)}))}}},De=ye,Se=(a("6a10"),Object(j["a"])(De,K,Q,!1,null,"1677c5c9",null)),Pe=Se.exports,_e={name:"workSpace",components:{showProcessInstanceImg:Y,FormHandler:Pe},data:function(){return{list:[],total:0,pageData:{count:10,currentPage:1}}},mounted:function(){this.queryList()},computed:{},methods:{showPic:function(e){this.$refs.showProcessInstanceImg.showDialog(e.processInstanceId)},handleClick:function(e){this.$refs.formHandler.show(e)},handleSizeChangePending:function(e){this.pageData.count=e,this.pageData.currentPage=1,this.queryList()},handleCurrentChangePending:function(e){this.pageData.currentPage=e,this.queryList()},initList:function(e){this.pageData.currentPage=e,this.queryList()},queryList:function(){var e=this,t=this;l({page:this.pageData.currentPage,count:this.pageData.count}).then((function(e){t.list=e.data.records,t.total=e.data.total})).catch((function(t){e.$message.error(t.response.data)}))}}},xe=_e,Ce=(a("b3cb"),Object(j["a"])(xe,A,H,!1,null,"661ed378",null)),Ne=Ce.exports,$e=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.list,border:""}},[a("el-table-column",{attrs:{prop:"groupName",label:"分组",width:"180"}}),a("el-table-column",{attrs:{prop:"name",label:"流程",width:"180"}}),a("el-table-column",{attrs:{prop:"createTime",label:"流程发起时间",width:"180"}}),a("el-table-column",{attrs:{prop:"endTime",label:"流程结束时间",width:"180"}}),a("el-table-column",{attrs:{label:"状态",width:"180"},scopedSlots:e._u([{key:"default",fn:function(t){return[1==t.row.status?a("el-tag",{attrs:{type:"success"}},[e._v("进行中")]):a("el-tag",[e._v("已结束")])]}}])}),a("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.handleClick(t.row)}}},[e._v("查看")]),1==t.row.status?a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.stop(t.row)}}},[e._v("终止流程")]):e._e(),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.showPic(t.row)}}},[e._v("流程图")])]}}])})],1),a("div",{staticStyle:{"text-align":"center",padding:"20px"}},[a("el-pagination",{attrs:{background:"",layout:"total, sizes, prev, pager, next, jumper","page-sizes":[10,20,50],"page-size":e.pageData.count,"current-page":e.pageData.currentPage,total:e.total},on:{"size-change":e.handleSizeChangePending,"current-change":e.handleCurrentChangePending}})],1),a("form-handler",{ref:"formHandler",on:{back:e.initList}}),a("showProcessInstanceImg",{ref:"showProcessInstanceImg"})],1)},Oe=[],Le={name:"workSpace",components:{FormHandler:Pe,showProcessInstanceImg:Y},data:function(){return{showRight:!1,drawerTitle:"",formItems:[],formData:{},list:[],total:0,pageData:{count:10,currentPage:1},nodeShow:[]}},mounted:function(){this.queryList()},computed:{},methods:{initList:function(e){},handleClick:function(e){this.$refs.formHandler.show(e,!1)},stop:function(e){var t=this;w({processInstanceId:e.processInstanceId}).then((function(e){t.queryList()}))},showPic:function(e){this.$refs.showProcessInstanceImg.showDialog(e.processInstanceId)},handleSizeChangePending:function(e){this.pageData.count=e,this.pageData.currentPage=1,this.queryList()},handleCurrentChangePending:function(e){this.pageData.currentPage=e,this.queryList()},queryList:function(){var e=this,t=this;u({page:this.pageData.currentPage,count:this.pageData.count}).then((function(e){t.list=e.data.records,t.total=e.data.total})).catch((function(t){e.$message.error(t.response.data)}))}}},Te=Le,je=(a("0da4"),Object(j["a"])(Te,$e,Oe,!1,null,"3acdaca4",null)),Ue=je.exports,Ee=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.list,border:""}},[a("el-table-column",{attrs:{prop:"groupName",label:"分组",width:"180"}}),a("el-table-column",{attrs:{prop:"processName",label:"流程",width:"180"}}),a("el-table-column",{attrs:{prop:"rootUserName",label:"发起人",width:"180"}}),a("el-table-column",{attrs:{prop:"startTime",label:"流程发起时间",width:"180"}}),a("el-table-column",{attrs:{prop:"taskName",label:"任务名称",width:"180"}}),a("el-table-column",{attrs:{prop:"taskCreateTime",label:"任务开始时间",width:"180"}}),a("el-table-column",{attrs:{prop:"taskEndTime",label:"任务结束时间",width:"180"}}),a("el-table-column",{attrs:{fixed:"right",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.handleClick(t.row)}}},[e._v("查看")]),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.showPic(t.row)}}},[e._v("流程图")])]}}])})],1),a("div",{staticStyle:{"text-align":"center",padding:"20px"}},[a("el-pagination",{attrs:{background:"",layout:"total, sizes, prev, pager, next, jumper","page-sizes":[10,20,50],"page-size":e.pageData.count,"current-page":e.pageData.currentPage,total:e.total},on:{"size-change":e.handleSizeChangePending,"current-change":e.handleCurrentChangePending}})],1),a("form-handler",{ref:"formHandler",on:{back:e.initList}}),a("showProcessInstanceImg",{ref:"showProcessInstanceImg"})],1)},ze=[],Fe={name:"workSpace",components:{showProcessInstanceImg:Y,FormHandler:Pe},data:function(){return{list:[],total:0,pageData:{count:10,currentPage:1}}},mounted:function(){this.queryList()},computed:{},methods:{showPic:function(e){this.$refs.showProcessInstanceImg.showDialog(e.processInstanceId)},handleClick:function(e){this.$refs.formHandler.show(e,!1)},handleSizeChangePending:function(e){this.pageData.count=e,this.pageData.currentPage=1,this.queryList()},handleCurrentChangePending:function(e){this.pageData.currentPage=e,this.queryList()},initList:function(e){this.pageData.currentPage=e,this.queryList()},queryList:function(){var e=this,t=this;d({page:this.pageData.currentPage,count:this.pageData.count}).then((function(e){t.list=e.data.records,t.total=e.data.total})).catch((function(t){e.$message.error(t.response.data)}))}}},Re=Fe,qe=(a("7ee0"),Object(j["a"])(Re,Ee,ze,!1,null,"1423ee62",null)),Ae=qe.exports,He=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.list,border:""}},[a("el-table-column",{attrs:{prop:"groupName",label:"分组",width:"180"}}),a("el-table-column",{attrs:{prop:"processName",label:"流程",width:"180"}}),a("el-table-column",{attrs:{prop:"startUserName",label:"发起人",width:"180"}}),a("el-table-column",{attrs:{prop:"startTime",label:"流程发起时间",width:"180"}}),a("el-table-column",{attrs:{prop:"nodeName",label:"当前节点",width:"180"}}),a("el-table-column",{attrs:{prop:"nodeTime",label:"抄送时间",width:"180"}}),a("el-table-column",{attrs:{fixed:"right",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.handleClick(t.row)}}},[e._v("查看")]),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.showPic(t.row)}}},[e._v("流程图")])]}}])})],1),a("div",{staticStyle:{"text-align":"center",padding:"20px"}},[a("el-pagination",{attrs:{background:"",layout:"total, sizes, prev, pager, next, jumper","page-sizes":[10,20,50],"page-size":e.pageData.count,"current-page":e.pageData.currentPage,total:e.total},on:{"size-change":e.handleSizeChangePending,"current-change":e.handleCurrentChangePending}})],1),a("el-drawer",{attrs:{"destroy-on-close":"",title:e.drawerTitle,visible:e.showRight,direction:"rtl"},on:{"update:visible":function(t){e.showRight=t}}},[a("div",{staticClass:"right-drawer-1",staticStyle:{padding:"5px","background-color":"lightgrey"}},[a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"10px"}},[a("form-render",{ref:"form",staticClass:"process-form",attrs:{forms:e.formItems},model:{value:e.formData,callback:function(t){e.formData=t},expression:"formData"}})],1)],1)]),a("showProcessInstanceImg",{ref:"showProcessInstanceImg"})],1)},Be=[],Me={name:"workSpace",components:{FormHandler:Pe,FormRender:_["a"],showProcessInstanceImg:Y},data:function(){return{showRight:!1,drawerTitle:"",formItems:[],formData:{},list:[],total:0,pageData:{count:10,currentPage:1}}},mounted:function(){this.queryList()},computed:{},methods:{showPic:function(e){this.$refs.showProcessInstanceImg.showDialog(e.processInstanceId)},handleClick:function(e){var t=this;console.log(e);var a=this;I({id:e.id}).then((function(t){a.drawerTitle=e.processName;var o,s=Object(r["a"])(t.data.formItems);try{for(s.s();!(o=s.n()).done;){var n=o.value;a.$set(a.formData,n.id,n.value)}}catch(i){s.e(i)}finally{s.f()}a.formItems=t.data.formItems,a.showRight=!0})).catch((function(e){t.$message.error(e.response.data)}))},handleSizeChangePending:function(e){this.pageData.count=e,this.pageData.currentPage=1,this.queryList()},handleCurrentChangePending:function(e){this.pageData.currentPage=e,this.queryList()},queryList:function(){var e=this,t=this;h({page:this.pageData.currentPage,count:this.pageData.count}).then((function(e){t.list=e.data.records,t.total=e.data.total})).catch((function(t){e.$message.error(t.response.data)}))}}},Je=Me,Ve=(a("a8b1"),Object(j["a"])(Je,He,Be,!1,null,"31380713",null)),Ge=Ve.exports,Ye=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{title:e.dialogTitle,width:"800px",visible:e.showDialog,"close-on-click-modal":!1},on:{"update:visible":function(t){e.showDialog=t}}},[e.showDialog?a("initiate-process",{ref:"processForm",attrs:{defaultFormData:e.defaultFormData,code:e.formId},on:{childEvent:e.selectNodeUser,childEvent1:e.removeNodeUser}}):e._e(),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showDialog=!1}}},[e._v("取 消")]),a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.submitForm}},[e._v("提 交")])],1)],1)],1)},Ke=[],Qe={name:"workSpace",components:{FormRender:_["a"],InitiateProcess:q},props:{dialogTitle:{type:String,default:"发起流程"},formId:String,paramId:String},data:function(){return{nodeDataUser:{},showDialog:!1,defaultFormData:{}}},mounted:function(){},computed:{},methods:{openDialog:function(e){e&&(this.defaultFormData=e),this.showDialog=!0},closeDialog:function(){this.showDialog=!1},selectNodeUser:function(e,t){this.nodeDataUser[t]=e[t]},removeNodeUser:function(){this.nodeDataUser={}},submitForm:function(){var e,t=this,a=this.$refs.processForm.formData,o=this.$refs.processForm.form,s=this,n=!0,i=o.selectUserNodeId,c=Object(r["a"])(i);try{for(c.s();!(e=c.n()).done;){var l=e.value,d=this.nodeDataUser[l];if(!(d&&d.length>0)){n=!1;break}}}catch(u){c.e(u)}finally{c.f()}n?this.$refs.processForm.validate((function(e){e?s.$emit("submitProcess",s.formId,a,s.paramId):t.$message.warning("请完善表单😥")})):this.$message.error("请完善节点信息")}}},We=Qe,Xe=Object(j["a"])(We,Ye,Ke,!1,null,"c18ccdc2",null),Ze=Xe.exports,et={name:"workSpace",components:{InitiateProcess:q,pending:Ne,started:Ue,cc:Ge,start:Ze,end:Ae},data:function(){return{selectForm:{},formItem:{},actives:[],formList:{list:[],inputs:"",searchResult:[]},nodeDataUser:{}}},mounted:function(){this.getGroups()},watch:{"formList.inputs":function(e){var t,a=[],o=Object(r["a"])(this.formList.list);try{for(o.s();!(t=o.n()).done;){var s,n=t.value,i=Object(r["a"])(n.items);try{for(i.s();!(s=i.n()).done;){var c=s.value,l=c.formName;console.log(l),l.indexOf(e)>-1&&a.push(c)}}catch(d){i.e(d)}finally{i.f()}}}catch(d){o.e(d)}finally{o.f()}console.log(a),this.formList.searchResult=a}},methods:{tabClick:function(e){"started"===e.name&&this.$refs[e.name].queryList(),"pending"===e.name&&this.$refs[e.name].queryList(),"cc"===e.name&&this.$refs[e.name].queryList(),"end"===e.name&&this.$refs[e.name].queryList()},selectNodeUser:function(e,t){this.nodeDataUser[t]=e[t]},removeNodeUser:function(){this.nodeDataUser={}},getGroups:function(){var e=this;Object(n["d"])({hidden:!1}).then((function(t){e.formList.list=t.data,e.formList.list.forEach((function(t){e.actives.push(t.name),t.items.forEach((function(e){e.logo=JSON.parse(e.logo)}))})),e.groups=t.data})).catch((function(t){return e.$message.error("获取分组异常")}))},enterItem:function(e){this.selectForm=e,this.$refs.startDialog.openDialog()},submitForm:function(e,t){var a=this,o=this;c({flowId:e,paramMap:t}).then((function(e){o.$refs.pending.pageData.currentPage=1,o.$refs.started.pageData.currentPage=1,o.$refs.pending.queryList(),o.$refs.started.queryList(),o.$refs.startDialog.closeDialog()})).catch((function(e){console.log(e),a.$message.error(e.response.data)}))}}},tt=et,at=(a("7257"),Object(j["a"])(tt,o,s,!1,null,"2c66edd8",null));t["default"]=at.exports},d471:function(e,t,a){},ee7e:function(e,t,a){}}]);
//# sourceMappingURL=chunk-2751ce98.90fa81e1.js.map