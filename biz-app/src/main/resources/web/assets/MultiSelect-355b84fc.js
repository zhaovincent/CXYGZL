import{d as f,D as c,h as g,a,c as m,l as u,w as h,R as v,a6 as y,f as k,as as _}from"./index-d5547550.js";import{a as b,E}from"./el-select-46753b34.js";import"./el-tag-de6752b8.js";import"./el-scrollbar-cbe0c54f.js";import"./el-popper-00977718.js";const V=f({__name:"MultiSelect",props:{mode:{type:String,default:"D"},form:{type:Object,default:()=>{}}},setup(r,{expose:p}){const n=r;var i=c({get(){let e=n.form.props.value;return e&&e.length>0?e.map(l=>l.key):void 0},set(e){let l=n.form.props.options.filter(o=>e.indexOf(o.key)>=0);n.form.props.value=l}});return g(),p({getValidateRule:()=>{var e=n.form,l=(d,t,s)=>e.required&&(t==null||t.length==0)?s(new Error("请选择"+e.name)):(t==null,s());let o=[{validator:l,trigger:"change"}];return e.required&&o.push({required:!0,message:"请选择"+e.name,trigger:"change"}),o}}),(e,l)=>{const o=b,d=E;return a(),m("div",null,[r.mode==="D"?(a(),u(o,{key:0,style:{width:"100%"},disabled:!0,placeholder:r.form?r.form.placeholder:"",size:"large"},null,8,["placeholder"])):(a(),u(o,{key:1,style:{width:"100%"},multiple:"","collapse-tags":"","collapse-tags-tooltip":"",modelValue:k(i),"onUpdate:modelValue":l[0]||(l[0]=t=>_(i)?i.value=t:i=t),disabled:r.form.perm==="R",placeholder:r.form.placeholder,size:"large"},{default:h(()=>[(a(!0),m(v,null,y(r.form.props.options,t=>(a(),u(d,{key:t.key,label:t.value,value:t.key},null,8,["label","value"]))),128))]),_:1},8,["modelValue","disabled","placeholder"]))])}}}),j=Object.freeze(Object.defineProperty({__proto__:null,default:V},Symbol.toStringTag,{value:"Module"}));export{j as _};
