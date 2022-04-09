package com.example.codingtest

import com.google.gson.annotations.SerializedName

class Payload(@SerializedName("a") var a: Int = -1,
              @SerializedName("p") var p: String = "",
              @SerializedName("q") var q: String = "",
              @SerializedName("f") var f: Int = -1,
              @SerializedName("l") var l: Int = -1,
              @SerializedName("T") var _T: Long = -1,
              @SerializedName("m") var m: Boolean = false,
              @SerializedName("M") var _M: Boolean = false)

class WebPayLoad(@SerializedName("e") var e: String = "",
                 @SerializedName("E") var _E: Long = -1,
                 @SerializedName("s") var s: String = "",
                 @SerializedName("a") var a: Int = -1,
                 @SerializedName("p") var p: String = "",
                 @SerializedName("q") var q: String = "",
                 @SerializedName("f") var f: Int = -1,
                 @SerializedName("l") var l: Int = -1,
                 @SerializedName("T") var _T: Long = -1,
                 @SerializedName("m") var m: Boolean = false,
                 @SerializedName("M") var _M: Boolean = false)