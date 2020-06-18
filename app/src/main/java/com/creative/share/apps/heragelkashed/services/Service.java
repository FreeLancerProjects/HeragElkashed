package com.creative.share.apps.heragelkashed.services;


import com.creative.share.apps.heragelkashed.models.AboutModel;
import com.creative.share.apps.heragelkashed.models.AdDataModel;
import com.creative.share.apps.heragelkashed.models.AdModel;
import com.creative.share.apps.heragelkashed.models.AdTypeDataModel;
import com.creative.share.apps.heragelkashed.models.CompanyAdDataModel;
import com.creative.share.apps.heragelkashed.models.CompanyDataModel;
import com.creative.share.apps.heragelkashed.models.FavoriteDataModel;
import com.creative.share.apps.heragelkashed.models.FormDataModel;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.MessageDataModel;
import com.creative.share.apps.heragelkashed.models.MessageModel;
import com.creative.share.apps.heragelkashed.models.MyAdsDataModel;
import com.creative.share.apps.heragelkashed.models.NotificationDataModel;
import com.creative.share.apps.heragelkashed.models.PlaceGeocodeData;
import com.creative.share.apps.heragelkashed.models.PlaceMapDetailsData;
import com.creative.share.apps.heragelkashed.models.ReportReasonDataModel;
import com.creative.share.apps.heragelkashed.models.RoomIdModel;
import com.creative.share.apps.heragelkashed.models.SearchDataModel;
import com.creative.share.apps.heragelkashed.models.SliderModelData;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.SubSubCategoryModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.models.UserRoomModelData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/login/or/register")
    Call<UserModel> login(@Field("mobile_code") String mobile_code,
                          @Field("mobile_number") String mobile_number,
                          @Field("software_type") int software_type
    );


    @FormUrlEncoded
    @POST("api/cofirm-code")
    Call<UserModel> confirmCode(@Field("user_id") int user_id,
                                @Field("confirmation_code") String confirmation_code
    );

    @FormUrlEncoded
    @POST("api/code/send")
    Call<UserModel> resendCode(@Field("user_id") int user_id);


    @GET("api/all-categories")
    Call<MainCategoryDataModel> getMainCategory();


    @FormUrlEncoded
    @POST("api/ads/feature")
    Call<SliderModelData> getSliderData(@Field("user_id") int user_id);


    @FormUrlEncoded
    @POST("api/single-ads/and/more")
    Call<AdModel> getAdDetails(@Field("user_id") int user_id,
                               @Field("ads_id") int ads_id
    );


    @GET("api/all-types")
    Call<AdTypeDataModel> getAdTypes();

    @FormUrlEncoded
    @POST("api/sub-category/children")
    Call<SubSubCategoryModel> getSubSubCategories(@Field("cat_id") int sub_id);

    @FormUrlEncoded
    @POST("api/ads/category/or/type")
    Call<AdDataModel> getAdsByType(@Field("cat_id") int cat_id,
                                   @Field("type_id") int type_id,
                                   @Field("user_id") int user_id,
                                   @Field("page") int page
    );

    @GET("api/sub-categories")
    Call<SubCategoryDataModel> getAllSubCategory();


    @FormUrlEncoded
    @POST("api/ads/search")
    Call<SearchDataModel> searchByName(@Field("name") String name,
                                       @Field("cat_id") int cat_id,
                                       @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("api/companies/subcategory")
    Call<CompanyDataModel> getCompanies(@Field("sub_cat_id") int sub_cat_id, @Field("page") int page);

    @FormUrlEncoded
    @POST("api/company/name")
    Call<CompanyDataModel> searchCompanyByName(@Field("name") String companyName,
                                               @Field("page") int page);


    @FormUrlEncoded
    @POST("api/myads")
    Call<CompanyAdDataModel> getCompanyAds(@Field("company_id") int company_id,
                                           @Field("page") int page);


    @FormUrlEncoded
    @POST("api/my-favorite-ads")
    Call<FavoriteDataModel> getFavoriteAds(@Field("user_id") int user_id,
                                           @Field("page") int page);


    @GET("api/app/info")
    Call<AboutModel> aboutApp();


    @FormUrlEncoded
    @POST("api/favourite/action")
    Call<ResponseBody> addFavoriteAds(@Field("user_id") int user_id,
                                      @Field("advertisement_id") int advertisement_id);

    @FormUrlEncoded
    @POST("api/like/action")
    Call<ResponseBody> addLikesAds(@Field("user_id") int user_id,
                                   @Field("advertisement_id") int advertisement_id);

    @FormUrlEncoded
    @POST("api/report/action")
    Call<ResponseBody> addReportAds(@Field("user_id") int user_id,
                                    @Field("advertisement_id") int advertisement_id,
                                    @Field("report_reason_id") int report_reason_id);

    @GET("api/all-report-reasons")
    Call<ReportReasonDataModel> getReportReasons();

    @FormUrlEncoded
    @POST("api/comment/add")
    Call<ResponseBody> addComment(@Field("user_id") int user_id,
                                  @Field("advertisement_id") int advertisement_id,
                                  @Field("comment") String comment);

    @FormUrlEncoded
    @POST("api/company/add")
    Call<ResponseBody> addCompany(@Field("name") String name,
                                  @Field("phone") String phone,
                                  @Field("email") String email,
                                  @Field("details") String details,
                                  @Field("latitude") double latitude,
                                  @Field("longitude") double longitude,
                                  @Field("category_id") int category_id,
                                  @Field("user_id") int user_id,
                                  @Field("address") String address
    );


    @GET("api/adv-form")
    Call<FormDataModel> getForms(@Query("cat_id") int cat_id);

    @Multipart
    @POST("api/create-adv")
    Call<ResponseBody> addAdsWithVideo(@Part("main_category_id") RequestBody main_category_id,
                                       @Part("category_id") RequestBody category_id,
                                       @Part("sub_id") RequestBody sub_id,
                                       @Part("user_id") RequestBody user_id,
                                       @Part("ad_title") RequestBody ad_title,
                                       @Part("ad_desc") RequestBody ad_desc,
                                       @Part("type_id") RequestBody type_id,
                                       @Part("city_id") RequestBody city_id,
                                       @Part("price") RequestBody price,
                                       @Part("address") RequestBody address,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude,
                                       @Part MultipartBody.Part ad_video,
                                       @Part List<MultipartBody.Part> ad_images,
                                       @PartMap() Map<String, RequestBody> map

    );

    @Multipart
    @POST("api/create-adv")
    Call<ResponseBody> addAdsWithoutVideo(@Part("main_category_id") RequestBody main_category_id,
                                          @Part("category_id") RequestBody category_id,
                                          @Part("sub_id") RequestBody sub_id,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("ad_title") RequestBody ad_title,
                                          @Part("ad_desc") RequestBody ad_desc,
                                          @Part("type_id") RequestBody type_id,
                                          @Part("city_id") RequestBody city_id,
                                          @Part("price") RequestBody price,
                                          @Part("address") RequestBody address,
                                          @Part("latitude") RequestBody latitude,
                                          @Part("longitude") RequestBody longitude,
                                          @Part List<MultipartBody.Part> ad_images,
                                          @PartMap() Map<String, RequestBody> map

    );

    @FormUrlEncoded
    @POST("api/myads")
    Call<MyAdsDataModel> getMyAds(@Field("user_id") int user_id,
                                  @Field("page") int page
    );


    @FormUrlEncoded
    @POST("api/my-notifications")
    Call<NotificationDataModel> getNotifications(@Field("user_id") int user_id,
                                                 @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/my-chat-rooms")
    Call<UserRoomModelData> getRooms(@Field("user_id") int user_id,
                                     @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/single-chat-room")
    Call<MessageDataModel> getRoomMessages(@Field("user_id") int user_id,
                                           @Field("room_id") int room_id,
                                           @Field("page") int page
    );


    @FormUrlEncoded
    @POST("api/message/send")
    Call<MessageModel> sendChatMessage(@Field("room_id") int room_id,
                                       @Field("from_user_id") int from_user_id,
                                       @Field("to_user_id") int to_user_id,
                                       @Field("message_type") int message_type,
                                       @Field("message") String message,
                                       @Field("date") long date


    );


    @Multipart
    @POST("api/message/send")
    Call<MessageModel> sendChatImage(@Part("room_id") RequestBody room_id,
                                     @Part("from_user_id") RequestBody from_user_id,
                                     @Part("to_user_id") RequestBody to_user_id,
                                     @Part("message_type") RequestBody message_type,
                                     @Part("message") RequestBody message,
                                     @Part("date") RequestBody date,
                                     @Part MultipartBody.Part image


    );


    @FormUrlEncoded
    @POST("api/chatRoom/get")
    Call<RoomIdModel> getRoomId(@Field("from_user_id") int from_user_id,
                                @Field("to_user_id") int to_user_id


    );


    @FormUrlEncoded
    @POST("api/phone-tokens")
    Call<ResponseBody> updateToken(@Field("user_id") int user_id,
                                   @Field("number_token") String number_token,
                                   @Field("software_type") int software_type


    );
}


