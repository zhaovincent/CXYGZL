import{b as T,d8 as q,d as N,aJ as A,u as B,D as O,a as x,l as V,w as y,e as P,n as j,f as k,m as F,_ as L,s as U,h as $,c as C,R as M,a5 as w,a3 as z,q as J}from"./index-e01dade3.js";import{E as K}from"./el-input-number-182b17d2.js";const G=T({type:{type:String,values:["primary","success","info","warning","danger",""],default:""},size:{type:String,values:q,default:""},truncated:{type:Boolean},tag:{type:String,default:"span"}}),H=N({name:"ElText"}),Q=N({...H,props:G,setup(r){const p=r,v=A(),u=B("text"),h=O(()=>[u.b(),u.m(p.type),u.m(v.value),u.is("truncated",p.truncated)]);return(_,e)=>(x(),V(F(_.tag),{class:j(k(h))},{default:y(()=>[P(_.$slots,"default")]),_:3},8,["class"]))}});var W=L(Q,[["__file","/home/runner/work/element-plus/element-plus/packages/components/text/src/text.vue"]]);const X=U(W);const Y=N({__name:"Money",props:{mode:{type:String,default:"D"},form:{type:Object,default:()=>{}}},setup(r,{expose:p}){const v=r;function u(e){if(e==null||e===void 0)return"";var t=new Array("零","壹","贰","叁","肆","伍","陆","柒","捌","玖"),c=new Array("","拾","佰","仟"),o=new Array("","万","亿","兆"),g=new Array("角","分","毫","厘"),s="整",a="元",D=1e15,m,d,n="",b;if(e==""||(e=parseFloat(e),e>=D))return"";if(e==0)return n=t[0]+a+s,n;if(e=e.toString(),e.indexOf(".")==-1?(m=e,d=""):(b=e.split("."),m=b[0],d=b[1].substr(0,4)),parseInt(m,10)>0){let f=0,i=m.length;for(let l=0;l<i;l++){let S=m.substr(l,1),E=i-l-1,R=E/4,I=E%4;S=="0"?f++:(f>0&&(n+=t[0]),f=0,n+=t[parseInt(S)]+c[I]),I==0&&f<4&&(n+=o[R])}n+=a}if(d!=""){let f=d.length;for(let i=0;i<f;i++){let l=d.substr(i,1);l!="0"&&(n+=t[Number(l)]+g[i])}}return n==""?n+=t[0]+a+s:d==""&&(n+=s),n}const{proxy:h}=$();return p({getValidateRule:()=>{var e=v.form,t=e.props,c=(g,s,a)=>e.required&&!s?a(new Error("请填写"+e.name)):s?t.radixNum&&h.$getNumberRadixNum(s)>t.radixNum?a(new Error("小数位数不能大于"+t.radixNum)):t.min&&s<t.min?a(new Error("数值不能小于"+t.min)):t.max&&s>t.max?a(new Error("数值不能大于"+t.max)):a():a();let o=[{validator:c,trigger:"blur"}];return e.required&&o.push({required:!0,message:"请填写"+e.name,trigger:"blur"}),o}}),(e,t)=>{const c=K,o=X;return x(),C("div",null,[r.mode==="D"?(x(),V(c,{key:0,style:{width:"100%"},"controls-position":"right",disabled:!0,placeholder:r.form?r.form.placeholder:""},null,8,["placeholder"])):(x(),C(M,{key:1},[w(c,{style:{width:"100%"},"controls-position":"right",modelValue:r.form.props.value,"onUpdate:modelValue":t[0]||(t[0]=g=>r.form.props.value=g),precision:r.form.props.radixNum,disabled:r.form.perm==="R",placeholder:r.form.placeholder},null,8,["modelValue","precision","disabled","placeholder"]),w(o,null,{default:y(()=>[z(" 大写： ")]),_:1}),w(o,{type:"info"},{default:y(()=>[z(J(u(r.form.props.value)),1)]),_:1})],64))])}}}),te=Object.freeze(Object.defineProperty({__proto__:null,default:Y},Symbol.toStringTag,{value:"Module"}));export{X as E,te as _};
