import{U as x,j as I,e as M,h as $,S as R,l as c}from"./_Uint8Array-c7447712.js";import{bW as K,bN as b,cf as J,bF as G,bO as N}from"./index-d5547550.js";var Q="__lodash_hash_undefined__";function X(n){return this.__data__.set(n,Q),this}function Y(n){return this.__data__.has(n)}function L(n){var e=-1,a=n==null?0:n.length;for(this.__data__=new K;++e<a;)this.add(n[e])}L.prototype.add=L.prototype.push=X;L.prototype.has=Y;function Z(n,e){for(var a=-1,f=n==null?0:n.length;++a<f;)if(e(n[a],a,n))return!0;return!1}function W(n,e){return n.has(e)}var m=1,h=2;function F(n,e,a,f,u,r){var s=a&m,g=n.length,l=e.length;if(g!=l&&!(s&&l>g))return!1;var t=r.get(n),v=r.get(e);if(t&&v)return t==e&&v==n;var _=-1,i=!0,p=a&h?new L:void 0;for(r.set(n,e),r.set(e,n);++_<g;){var d=n[_],A=e[_];if(f)var T=s?f(A,d,_,e,n,r):f(d,A,_,n,e,r);if(T!==void 0){if(T)continue;i=!1;break}if(p){if(!Z(e,function(O,w){if(!W(p,w)&&(d===O||u(d,O,a,f,r)))return p.push(w)})){i=!1;break}}else if(!(d===A||u(d,A,a,f,r))){i=!1;break}}return r.delete(n),r.delete(e),i}function z(n){var e=-1,a=Array(n.size);return n.forEach(function(f,u){a[++e]=[u,f]}),a}function j(n){var e=-1,a=Array(n.size);return n.forEach(function(f){a[++e]=f}),a}var o=1,V=2,k="[object Boolean]",nn="[object Date]",en="[object Error]",rn="[object Map]",an="[object Number]",fn="[object RegExp]",sn="[object Set]",un="[object String]",ln="[object Symbol]",gn="[object ArrayBuffer]",tn="[object DataView]",B=b?b.prototype:void 0,S=B?B.valueOf:void 0;function _n(n,e,a,f,u,r,s){switch(a){case tn:if(n.byteLength!=e.byteLength||n.byteOffset!=e.byteOffset)return!1;n=n.buffer,e=e.buffer;case gn:return!(n.byteLength!=e.byteLength||!r(new x(n),new x(e)));case k:case nn:case an:return J(+n,+e);case en:return n.name==e.name&&n.message==e.message;case fn:case un:return n==e+"";case rn:var g=z;case sn:var l=f&o;if(g||(g=j),n.size!=e.size&&!l)return!1;var t=s.get(n);if(t)return t==e;f|=V,s.set(n,e);var v=F(g(n),g(e),f,u,r,s);return s.delete(n),v;case ln:if(S)return S.call(n)==S.call(e)}return!1}var dn=1,An=Object.prototype,vn=An.hasOwnProperty;function pn(n,e,a,f,u,r){var s=a&dn,g=I(n),l=g.length,t=I(e),v=t.length;if(l!=v&&!s)return!1;for(var _=l;_--;){var i=g[_];if(!(s?i in e:vn.call(e,i)))return!1}var p=r.get(n),d=r.get(e);if(p&&d)return p==e&&d==n;var A=!0;r.set(n,e),r.set(e,n);for(var T=s;++_<l;){i=g[_];var O=n[i],w=e[i];if(f)var D=s?f(w,O,i,e,n,r):f(O,w,i,n,e,r);if(!(D===void 0?O===w||u(O,w,a,f,r):D)){A=!1;break}T||(T=i=="constructor")}if(A&&!T){var y=n.constructor,P=e.constructor;y!=P&&"constructor"in n&&"constructor"in e&&!(typeof y=="function"&&y instanceof y&&typeof P=="function"&&P instanceof P)&&(A=!1)}return r.delete(n),r.delete(e),A}var Tn=1,C="[object Arguments]",U="[object Array]",E="[object Object]",On=Object.prototype,H=On.hasOwnProperty;function wn(n,e,a,f,u,r){var s=G(n),g=G(e),l=s?U:M(n),t=g?U:M(e);l=l==C?E:l,t=t==C?E:t;var v=l==E,_=t==E,i=l==t;if(i&&$(n)){if(!$(e))return!1;s=!0,v=!1}if(i&&!v)return r||(r=new R),s||c(n)?F(n,e,a,f,u,r):_n(n,e,l,a,f,u,r);if(!(a&Tn)){var p=v&&H.call(n,"__wrapped__"),d=_&&H.call(e,"__wrapped__");if(p||d){var A=p?n.value():n,T=d?e.value():e;return r||(r=new R),u(A,T,a,f,r)}}return i?(r||(r=new R),pn(n,e,a,f,u,r)):!1}function q(n,e,a,f,u){return n===e?!0:n==null||e==null||!N(n)&&!N(e)?n!==n&&e!==e:wn(n,e,a,f,q,u)}function En(n,e){return q(n,e)}export{L as S,q as b,W as c,En as i,j as s};
