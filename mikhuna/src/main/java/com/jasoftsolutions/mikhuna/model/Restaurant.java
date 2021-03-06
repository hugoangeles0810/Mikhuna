package com.jasoftsolutions.mikhuna.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.jasoftsolutions.mikhuna.domain.RestaurantServiceType;

import java.util.List;

public class Restaurant extends AbstractModel implements
        ClusterItem, Parcelable{

    @SerializedName("n")
	private String name;

    @SerializedName("d")
    private String description;

    @SerializedName("p")
    private String phoneNumber;

    @SerializedName("ma")
    private Float minAmount;

    @SerializedName("r")
	private Float rating;

    @SerializedName("iid")
    private Integer imageId;

    @SerializedName("mdt")
    private Integer minDeliveryTime;

    @SerializedName("mxdt")
    private Integer maxDeliveryTime;

    @SerializedName("tt")
    private List<RestaurantTimetable> restaurantTimetables;

    @SerializedName("la")
    private Double latitude;

    @SerializedName("lo")
    private Double longitude;

    @SerializedName("a")
    private String address;

    @SerializedName("sc")
    private Float shippingCost;

    @SerializedName("c")
    private String currency;

    @SerializedName("img_s")
    private String smallLogoUrl;

    @SerializedName("img_m")
    private String logoUrl;

    @SerializedName("w")
    private Integer weight;

    @SerializedName("sid")
    private Integer serviceTypeId;

    @SerializedName("uid")
    private Long ubigeoServerId;

    @SerializedName("rc")
    private List<RestaurantCategoryAssign> restaurantCategoryAssigns;

    @SerializedName("dtt")
    private String timetableDescription;

    @SerializedName("npc")
    private Integer numberProductCategory;

    private Long categoryLastUpdate;

    private Long likeDishLastUpdate;

    @SerializedName("__local_restaurantCategoryAssigns")    // reemplazo de "exclude"
    private List<RestaurantCategory> restaurantCategories;

    @SerializedName("__local_open")
    private Boolean open;

    private long lastOpenClosedCalc;

    @SerializedName("rdc")
    private List<RestaurantDishCategory> restaurantDishCategories;

    @SerializedName("link")
    private List<Link> links;

    @SerializedName("pay")
    private List<Pay> payMethods;

    @SerializedName("servi")
    private List<Service> services;

    @SerializedName("s")
    private Integer state;

    @SerializedName("img_l")
    private String imageUrl;

    @SerializedName("of")
    private Integer orientation;

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant() {
    }

    public Restaurant(Parcel in){
        setServerId(in.readLong());
        setName(in.readString());
        setLatitude(in.readDouble());
        setLongitude(in.readDouble());
        setImageId(in.readInt());
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Float getRating() {
		return rating;
	}
	public void setRating(Float rating) {
		this.rating = rating;
	}

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Float getMinAmount() {
        return minAmount;
    }
    public void setMinAmount(Float minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getImageId() {
        return imageId;
    }
    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public List<RestaurantTimetable> getRestaurantTimetables() {
        return restaurantTimetables;
    }
    public void setRestaurantTimetables(List<RestaurantTimetable> restaurantTimetables) {
        this.restaurantTimetables = restaurantTimetables;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getMinDeliveryTime() {
        return minDeliveryTime;
    }
    public void setMinDeliveryTime(Integer minDeliveryTime) {
        this.minDeliveryTime = minDeliveryTime;
    }

    public Integer getMaxDeliveryTime() {
        return maxDeliveryTime;
    }
    public void setMaxDeliveryTime(Integer maxDeliveryTime) {
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Float getShippingCost() {
        return shippingCost;
    }
    public void setShippingCost(Float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }
    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }
    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public List<RestaurantCategory> getRestaurantCategories() {
        return restaurantCategories;
    }
    public void setRestaurantCategories(List<RestaurantCategory> restaurantCategories) {
        this.restaurantCategories = restaurantCategories;
    }

    public Long getUbigeoServerId() {
        return ubigeoServerId;
    }
    public void setUbigeoServerId(Long ubigeoServerId) {
        this.ubigeoServerId = ubigeoServerId;
    }

    public List<RestaurantCategoryAssign> getRestaurantCategoryAssigns() {
        return restaurantCategoryAssigns;
    }
    public void setRestaurantCategoryAssigns(List<RestaurantCategoryAssign> restaurantCategoryAssigns) {
        this.restaurantCategoryAssigns = restaurantCategoryAssigns;
    }

    public Boolean isOpen() {
        return open;
    }
    public void setOpen(Boolean open) {
        this.open = open;
    }

    public long getLastOpenClosedCalc() {
        return lastOpenClosedCalc;
    }
    public void setLastOpenClosedCalc(long lastOpenClosedCalc) {
        this.lastOpenClosedCalc = lastOpenClosedCalc;
    }

    public String getTimetableDescription() {
        return timetableDescription;
    }
    public void setTimetableDescription(String timetableDescription) {
        this.timetableDescription = timetableDescription;
    }

    public Integer getNumberProductCategory() { return numberProductCategory; }
    public void setNumberProductCategory(Integer numberProductCategory) {
        this.numberProductCategory = numberProductCategory;
    }

    public Long getCategoryLastUpdate() {
        return categoryLastUpdate;
    }
    public void setCategoryLastUpdate(Long categoryLastUpdate) {
        this.categoryLastUpdate = categoryLastUpdate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Pay> getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(List<Pay> payMethods) {
        this.payMethods = payMethods;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public boolean isDelivery() {
        if (serviceTypeId == null) {
            return false;
        }
        return RestaurantServiceType.DELIVERY.match(serviceTypeId);
    }

    public boolean isBooking() {
        if (serviceTypeId == null) {
            return false;
        }
        return RestaurantServiceType.BOOKING.match(serviceTypeId);
    }

    public List<RestaurantDishCategory> getRestaurantDishCategories() {
        return restaurantDishCategories;
    }
    public void setRestaurantDishCategories(List<RestaurantDishCategory> restaurantDishCategories) {
        this.restaurantDishCategories = restaurantDishCategories;
    }

    public Long getLikeDishLastUpdate() {
        return likeDishLastUpdate;
    }

    public void setLikeDishLastUpdate(Long likeDishLastUpdate) {
        this.likeDishLastUpdate = likeDishLastUpdate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getServerId());
        dest.writeString(getName());
        dest.writeDouble(getLatitude());
        dest.writeDouble(getLongitude());
        dest.writeInt(getImageId());
    }
}
