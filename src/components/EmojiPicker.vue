<template>
  <div class="emoji-picker">
    <div class="emoji-categories">
      <button
        v-for="category in categories"
        :key="category.id"
        class="category-btn"
        :class="{ active: currentCategory === category.id }"
        @click="currentCategory = category.id"
      >
        {{ category.icon }}
      </button>
    </div>
    <div class="emoji-list">
      <button
        v-for="emoji in currentEmojis"
        :key="emoji"
        class="emoji-btn"
        @click="$emit('select', emoji)"
      >
        {{ emoji }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmojiPicker',
  data() {
    return {
      currentCategory: 'smileys',
      categories: [
        { id: 'smileys', icon: '😊', emojis: ['😀', '😃', '😄', '😁', '😅', '😂', '🤣', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘'] },
        { id: 'gestures', icon: '👋', emojis: ['👋', '🤚', '🖐', '✋', '🖖', '👌', '🤌', '🤏', '✌', '🤞', '🤟', '🤘', '🤙', '👈', '👉', '👆'] },
        { id: 'hearts', icon: '❤️', emojis: ['❤️', '🧡', '💛', '💚', '💙', '💜', '🤎', '🖤', '🤍', '💔', '❤️‍🔥', '❤️‍🩹', '💖', '💗', '💓', '💝'] },
        { id: 'animals', icon: '🐶', emojis: ['🐶', '🐱', '🐭', '🐹', '🐰', '🦊', '🐻', '🐼', '🐨', '🐯', '🦁', '🐮', '🐷', '🐸', '🐵', '🐔'] },
        { id: 'food', icon: '🍎', emojis: ['🍎', '🍐', '🍊', '🍋', '🍌', '🍉', '🍇', '🍓', '🫐', '🍈', '🍒', '🍑', '🥭', '🍍', '🥥', '🥝'] }
      ]
    }
  },
  computed: {
    currentEmojis() {
      return this.categories.find(c => c.id === this.currentCategory)?.emojis || []
    }
  }
}
</script>

<style scoped>
.emoji-picker {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  width: 320px;
  overflow: hidden;
}

.emoji-categories {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 8px;
  background: #f8f8f8;
}

.category-btn {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.category-btn:hover {
  background: rgba(0, 174, 236, 0.1);
}

.category-btn.active {
  background: rgba(0, 174, 236, 0.2);
}

.emoji-list {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 4px;
  padding: 8px;
  max-height: 200px;
  overflow-y: auto;
}

.emoji-btn {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
  font-size: 20px;
}

.emoji-btn:hover {
  background: rgba(0, 174, 236, 0.1);
}

/* 自定义滚动条样式 */
.emoji-list::-webkit-scrollbar {
  width: 6px;
}

.emoji-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.emoji-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.emoji-list::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style> 