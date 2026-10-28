package ru.urfu.studyassistant.infrastructure.vk;

public class VkConfig {

    private final String groupId;
    private final String accessToken;

    public VkConfig(String groupId, String accessToken) {
        if (groupId == null || groupId.isBlank()) {
            throw new IllegalArgumentException("groupId must not be blank");
        }
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }
        this.groupId = groupId;
        this.accessToken = accessToken;
    }

    public static VkConfig fromValues(String groupId, String accessToken) {
        return new VkConfig(groupId, accessToken);
    }

    public String groupId() {
        return groupId;
    }

    public String accessToken() {
        return accessToken;
    }
}