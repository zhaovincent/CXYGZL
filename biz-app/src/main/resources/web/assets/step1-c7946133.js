import{d as E,h as k,K as y,ab as q,r as w,o as L,L as S,D as F,a as n,c as d,k as U,a5 as t,w as m,f as a,ae as B,R as C,a6 as N,l as h,aA as M}from"./index-e01dade3.js";import{E as R}from"./el-card-16bdf0d8.js";/* empty css                */import"./el-tag-929563f7.js";import{E as $,a as A}from"./el-select-c154265f.js";import"./el-scrollbar-f2029ad1.js";import"./el-popper-ee5283d6.js";import{a as D,E as G}from"./el-form-item-85be6a3a.js";import{q as K}from"./index-cffc2a4a.js";import{S as O}from"./SingleUpload-dfe85bb8.js";import{_ as j}from"./selectAndShow.vue_vue_type_script_setup_true_lang-a708354f.js";import{u as z}from"./flow-2349aa77.js";import{_ as H}from"./_plugin-vue_export-helper-c27b6911.js";import"./strings-6464b592.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./debounce-bf37b7f7.js";import"./index-6b517de5.js";import"./_baseClone-59dac119.js";import"./el-progress-be452bc9.js";/* empty css                */import"./plus-95522b44.js";import"./index-f7c9196e.js";import"./employeesDialog-3166dde1.js";import"./el-dialog-1471a1af.js";import"./refs-dd65b3fd.js";import"./selectBox-a8819872.js";import"./el-avatar-63a3488d.js";import"./el-checkbox-123bf6dd.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";import"./index-8f0c79b2.js";import"./index-126d0406.js";import"./selectResult-ca10c7f8.js";import"./orgItem-da1d9713.js";const J={class:"container-div"},P=E({__name:"step1",props:{groupId:{type:Number,default:void 0}},setup(g,{expose:_}){const f=g,{proxy:c}=k();_({validate:p=>{c.$refs.ruleForm.validate((e,l)=>{var s=[];if(!e)for(var i in l)s.push(l[i][0].message);p(e,s)})}});const v=y({name:[{required:!0,message:"请填写名称",trigger:"blur"},{min:2,max:10,message:"2-10个字符",trigger:"blur"}],remark:[{required:!1,message:"请填写描述",trigger:"blur"},{min:2,max:20,message:"2-20个字符",trigger:"blur"}],groupId:[{required:!0,message:"请选择分组",trigger:"change"}],logo:[{required:!0,message:"请上传图标",trigger:"change"}],admin:[{required:!0,message:"请选择管理员",trigger:"change"}]});q();const u=w([]);L(()=>{K().then(({data:p})=>{u.value=p})}),S(()=>f.groupId,p=>{p&&(r.value.groupId=p)});let b=z();var r=F(()=>b.step1);return(p,e)=>{const l=D,s=M,i=$,V=A,x=G,I=R;return n(),d("div",null,[U("div",J,[t(I,{class:"box-card",style:{"padding-right":"10%","padding-left":"10%","margin-top":"20px"}},{default:m(()=>[t(x,{ref:"ruleForm",model:a(r),rules:v,"label-position":"top","status-icon":"","label-width":"120px",onSubmit:e[5]||(e[5]=B(()=>{},["prevent"]))},{default:m(()=>[t(l,{label:"图标",prop:"logo"},{default:m(()=>[t(O,{modelValue:a(r).logo,"onUpdate:modelValue":e[0]||(e[0]=o=>a(r).logo=o)},null,8,["modelValue"])]),_:1}),t(l,{label:"名称",prop:"name"},{default:m(()=>[t(s,{modelValue:a(r).name,"onUpdate:modelValue":e[1]||(e[1]=o=>a(r).name=o)},null,8,["modelValue"])]),_:1}),t(l,{label:"说明",prop:"remark"},{default:m(()=>[t(s,{modelValue:a(r).remark,"onUpdate:modelValue":e[2]||(e[2]=o=>a(r).remark=o)},null,8,["modelValue"])]),_:1}),t(l,{label:"分组",prop:"groupId"},{default:m(()=>[t(V,{modelValue:a(r).groupId,"onUpdate:modelValue":e[3]||(e[3]=o=>a(r).groupId=o),placeholder:"请选择流程组"},{default:m(()=>[(n(!0),d(C,null,N(u.value,o=>(n(),h(i,{key:o.id,label:o.groupName,value:o.id},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])]),_:1}),t(l,{label:"管理员",prop:"admin"},{default:m(()=>[t(j,{orgList:a(r).admin,"onUpdate:orgList":e[4]||(e[4]=o=>a(r).admin=o),type:"user",multiple:!1},null,8,["orgList"])]),_:1})]),_:1},8,["model","rules"])]),_:1})])])}}});const Be=H(P,[["__scopeId","data-v-0704664e"]]);export{Be as default};
